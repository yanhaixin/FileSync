package com.sac.connection;

import java.util.ArrayList;
import java.util.Vector;
import java.util.function.Predicate;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class SftpChannel implements AutoCloseable {

	private ChannelSftp channelSftp;

	@Override
	public void close() throws Exception {
		channelSftp.disconnect();
	}

	protected SftpChannel(ChannelSftp _channelSftp) throws JSchException {
		channelSftp = _channelSftp;
		channelSftp.connect();
	}

	public void getAndSave(String src, String dst) throws JSchException, SftpException {
		channelSftp.get(src, dst);
	}

	public ArrayList<LsEntry> ls(String path) throws SftpException, JSchException {
		return ls(path, LSSelector::defaultSelector);
	}

	public ArrayList<LsEntry> ls(String path, Predicate<LsEntry> predicate) throws SftpException, JSchException {
		ArrayList<LsEntry> rvalue = new ArrayList<LsEntry>();
		Vector<?> filelist = channelSftp.ls(path);
		for (Object entry : filelist) {
			LsEntry lsEntry = (LsEntry) entry;
			if (predicate.test(lsEntry))
				rvalue.add(lsEntry);
		}
		return rvalue;
	}
}
