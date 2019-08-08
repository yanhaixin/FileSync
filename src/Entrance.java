import com.sac.common.Log;
import com.sac.db.DBMgr;
import com.sac.file.FileCollection;
import com.sac.file.Proprieties;
import com.sac.file.Utility;
import com.sac.fileSync.task.SyncTask;
import com.sac.task.SimpleTM;
import com.sac.task.ThreadMgr;

public class Entrance {
	public static void testcode() {
		FileCollection fc = Utility.readFileList("C:\\Users\\yanhaixin\\eclipse-workspace\\FileSync\\lib");
		fc.forEach(x -> {
			Log.logger.info(x.path);
			Log.logger.info(x.name);
		});
	}

	private static SimpleTM<SyncTask> tm = null;

	private static void startup() {
		tm = new SimpleTM<SyncTask>(1, true);
		long interval = Long.parseLong(Proprieties.getInstance().getValue("interval"));
		SyncTask t = new SyncTask(0, interval * 1000);
		tm.add(t);
		// 注册监听进程停止事件，当执行kill -15执行
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			Log.logger.warn(Thread.currentThread().getName() + ": shutdown");
			Entrance.stop();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
		}));
	}

	private static void stop() {
		ThreadMgr.destory();
		DBMgr.close();
	}

	public static void main(String[] args) {
		startup();
	}

}
