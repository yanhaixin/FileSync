package com.sac.connection;

import com.jcraft.jsch.ChannelSftp.LsEntry;

public class FileSelector extends DefaultSelector {
	public boolean select(LsEntry entry) {
		return super.select(entry) && !entry.getAttrs().isDir();
	}
}
