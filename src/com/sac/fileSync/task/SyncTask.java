package com.sac.fileSync.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.sac.common.Log;
import com.sac.connection.LSSelector;
import com.sac.connection.SFTP;
import com.sac.file.DataFile;
import com.sac.file.FileCollection;
import com.sac.file.GlobalProperties;
import com.sac.file.Utility;
import com.sac.task.ScheduledTask;

public class SyncTask extends ScheduledTask {

	private String remotePath = null;
	private String localPath = null;
	private SFTP sftp = null;

	public SyncTask(Date time, long _interval) {
		super(time, _interval);
		init();
	}

	public SyncTask(long _delay, long _interval) {
		super(_delay, _interval);
		init();
	}

	private void init() {
		Properties config = GlobalProperties.getProperties();
		remotePath = config.getProperty("remotePath");
		localPath = config.getProperty("localPath");
		sftp = new SFTP(config.getProperty("host"), config.getProperty("user"), config.getProperty("passwd"),
				Integer.parseInt(config.getProperty("port")));
	}

	@Override
	protected void exec() throws Exception {
		try {
			FileCollection remote = new FileCollection();
			getRemoteFileRecusive(sftp, remotePath, remote, false);
			FileCollection local = Utility.readFileList(localPath);
			ArrayList<DataFile> filelist = Utility.compareAndGet(remote, local);
			filelist.forEach(x -> {
				Log.logger.info(x.name);
				try {
					sftp.getAndSave(x.getFullPath(), localPath);
				} catch (SftpException | JSchException e) {
					Log.logger.error(e.getMessage());
				}
			});
		} catch (SftpException e) {
			Log.logger.error(e.getMessage());
		}
		sftp.disconnectChannel();
	}

	protected void getRemoteFileRecusive(SFTP sftp, String path, FileCollection rval, boolean ifAdd)
			throws SftpException, JSchException {
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
