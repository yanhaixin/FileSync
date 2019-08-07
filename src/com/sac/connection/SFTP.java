package com.sac.connection;

import java.util.ArrayList;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.sac.common.Log;

public class SFTP {
	private String host;
	private String user;
	private String password;
	private int port;

	private Session session = null;
	private Channel channel = null;
	private ChannelSftp channelSftp = null;

	public SFTP(String _host, String _user, String _password, int _port) {
		host = _host;
		user = _user;
		password = _password;
		port = _port;
	}

	public void connect() {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(user, host, port);
			session.setPassword(password);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
		} catch (Exception ex) {
			Log.logger.error(ex.getMessage());

		}
	}

	public void disConnect() {
		if (channel != null)
			channel.disconnect();
		if (session != null)
			session.disconnect();
	}

	public void getAndSave(String src, String dst) throws SftpException {
		channelSftp.get(src, dst);
	}

	public ArrayList<LsEntry> ls(String path) throws SftpException {
		return ls(path, new DefaultSelector());
	}

	public ArrayList<LsEntry> ls(String path, LSSelector selector) throws SftpException {
		ArrayList<LsEntry> rvalue = new ArrayList<LsEntry>();
		if (channelSftp != null) {
			Vector<?> filelist = channelSftp.ls(path);
			for (Object entry : filelist) {
				LsEntry lsEntry = (LsEntry) entry;
				if (selector.select(lsEntry))
					rvalue.add(lsEntry);
			}
		}
		return rvalue;
	}

	protected void finalize() {
		disConnect();
	}
}
