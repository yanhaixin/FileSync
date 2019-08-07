package com.sac.connection;

import com.jcraft.jsch.ChannelSftp.LsEntry;

public class DefaultSelector implements LSSelector {

	@Override
	public boolean select(LsEntry entry) {
		return !(entry.getFilename().equals(".") || entry.getFilename().equals(".."));
	}

}
