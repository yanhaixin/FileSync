package com.sac.file;

import java.util.Date;

public class DataFile {
	public String path;
	public String name;
	public long size;
	public Date last_modification_time;

	public String getFullPath() {
		return path + '/' + name;
	}

	public DataFile(String _path, String _name, long _size, long date) {
		path = _path;
		name = _name;
		size = _size;
		last_modification_time = new Date(date);
	}
}
