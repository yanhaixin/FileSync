package com.sac.connection;

import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Predicate;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.sac.common.Log;

public class SFTP {
	private String host;
	private String user;
	private String password;
	private int port;

	private Session session = null;
	private JSch jsch = new JSch();
	private ChannelSftp channelSftp = null;
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
			channelSftp = null;
		}
		return session;
	}

	private ChannelSftp getchannelSftp() throws JSchException {
		if (channelSftp == null) {
			channelSftp = (ChannelSftp) getSession().openChannel("sftp");
			channelSftp.connect();
		}
		return channelSftp;
	}

	public void connect() {
		try {
			getSession();
		} catch (Exception ex) {
			Log.logger.error(ex.getMessage());
		}
	}

	public void disconnectChannel() {
		if (channelSftp != null)
			channelSftp.disconnect();
		channelSftp = null;
	}

	public void disconnect() {
		if (channelSftp != null)
			channelSftp.disconnect();
		if (session != null)
			session.disconnect();
		session = null;
		channelSftp = null;
	}

	public void getAndSave(String src, String dst) throws JSchException, SftpException {
		getchannelSftp().get(src, dst);
	}

	public ArrayList<LsEntry> ls(String path) throws SftpException, JSchException {
		return ls(path, LSSelector::defaultSelector);
	}

	public ArrayList<LsEntry> ls(String path, Predicate<LsEntry> predicate) throws SftpException, JSchException {
		ArrayList<LsEntry> rvalue = new ArrayList<LsEntry>();
		Vector<?> filelist = getchannelSftp().ls(path);
		for (Object entry : filelist) {
			LsEntry lsEntry = (LsEntry) entry;
			if (predicate.test(lsEntry))
				rvalue.add(lsEntry);
		}
		return rvalue;
	}

	protected void finalize() {
		disconnect();
	}
}
