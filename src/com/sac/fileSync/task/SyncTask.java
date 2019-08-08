package com.sac.fileSync.task;

import java.util.ArrayList;
import java.util.Date;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import com.sac.common.Log;
import com.sac.connection.LSSelector;
import com.sac.connection.SFTP;
import com.sac.file.DataFile;
import com.sac.file.FileCollection;
import com.sac.file.Proprieties;
import com.sac.file.Utility;
import com.sac.task.ScheduledTask;

public class SyncTask extends ScheduledTask {

	private String host = null;
	private String user = null;
	private String passwd = null;
	private int port = 22;
	private String remotePath = null;
	private String localPath = null;

	public SyncTask(Date time, long _interval) {
		super(time, _interval);
		init();
	}

	public SyncTask(long _delay, long _interval) {
		super(_delay, _interval);
		init();
	}

	private void init() {
		Proprieties ins = Proprieties.getInstance();
		host = ins.getValue("host");
		user = ins.getValue("user");
		passwd = ins.getValue("passwd");
		remotePath = ins.getValue("remotePath");
		localPath = ins.getValue("localPath");
		port = Integer.parseInt(ins.getValue("port"));
	}

	@Override
	protected void exec() throws Exception {
		SFTP sftp = new SFTP(host, user, passwd, port);
		sftp.connect();
		try {
			FileCollection remote = new FileCollection();
			getRemoteFileRecusive(sftp, remotePath, remote, false);
			FileCollection local = Utility.readFileList(localPath);
			ArrayList<DataFile> filelist = Utility.compareAndGet(remote, local);
			// sftp.getAndSave("/home/sac/project/" + entry.getFilename(), "C:/test");
			filelist.forEach(x -> {
				Log.logger.info(x.name);
				try {
					sftp.getAndSave(x.getFullPath(), localPath);
				} catch (SftpException e) {
					Log.logger.error(e.getMessage());
				}
			});
		} catch (SftpException e) {
			Log.logger.error(e.getMessage());
		}
		sftp.disConnect();
	}

	protected void getRemoteFileRecusive(SFTP sftp, String path, FileCollection rval, boolean ifAdd)
			throws SftpException {
		if (ifAdd) {
			ArrayList<LsEntry> filelist = sftp.ls(path, LSSelector::fileSelector);
			filelist.forEach(x -> rval.put(new DataFile(path, x.getFilename())));
		}
		ArrayList<LsEntry> dirlist = sftp.ls(path, LSSelector::dirSelector);
		for (LsEntry lsEntry : dirlist) {
			getRemoteFileRecusive(sftp, path + '/' + lsEntry.getFilename(), rval, true);
		}
	}
}
