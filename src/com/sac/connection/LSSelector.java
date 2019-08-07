package com.sac.connection;

import com.jcraft.jsch.ChannelSftp.LsEntry;

public interface LSSelector {
	boolean select(LsEntry entry);
}
