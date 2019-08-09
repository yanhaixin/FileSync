package com.sac.connection;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sac.common.Log;

public class SFTP {
	private String host;
	private String user;
	private String password;
	private int port;

	private Session session = null;
	private JSch jsch = new JSch();
	private java.util.Properties config = new java.util.Properties();

	public SFTP(String _host, String _user, String _password, int _port) {
		host = _host;
		user = _user;
		password = _password;
		port = _port;
		config.put("StrictHostKeyChecking", "no");
		config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
	}

	private Session getSession() throws JSchException {
		try {
			ChannelExec testChannel = (ChannelExec) session.openChannel("exec");
			testChannel.setCommand("true");
			testChannel.connect();
			Log.logger.info("Session created");
			testChannel.disconnect();
		} catch (Throwable t) {
			Log.logger.info("Session failed. connecting");
			session = jsch.getSession(user, host, port);
			session.setConfig(config);
			session.setPassword(password);
			session.connect();
		}
		return session;
	}

	public SftpChannel getSftpChannel() throws JSchException {
		return new SftpChannel((ChannelSftp) getSession().openChannel("sftp"));
	}

	public void close() {
		if (session != null)
			session.disconnect();
		session = null;
	}

	protected void finalize() {
		close();
	}
}
