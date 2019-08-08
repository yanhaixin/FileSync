package com.sac.connection;

import com.jcraft.jsch.ChannelSftp.LsEntry;

public class LSSelector {
	public static boolean defaultSelector(LsEntry entry) {
		return !(entry.getFilename().equals(".") || entry.getFilename().equals(".."));
	}

	public static boolean fileSelector(LsEntry entry) {
		return defaultSelector(entry) && !entry.getAttrs().isDir();
	}

	public static boolean dirSelector(LsEntry entry) {
		return defaultSelector(entry) && entry.getAttrs().isDir();
	}
}
