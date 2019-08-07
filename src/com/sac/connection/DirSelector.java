package com.sac.connection;

import com.jcraft.jsch.ChannelSftp.LsEntry;

public class DirSelector extends DefaultSelector {
	public boolean select(LsEntry entry) {
		return super.select(entry) && entry.getAttrs().isDir();
	}
}
