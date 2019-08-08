package com.sac.file;

public class DataFile {
	public String path;
	public String name;

	public String getFullPath() {
		return path + '/' + name;
	}

	public DataFile(String _path, String _name) {
		path = _path;
		name = _name;
	}
}
