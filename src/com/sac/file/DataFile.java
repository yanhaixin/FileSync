package com.sac.file;

public class DataFile {
	public String path;
	public String name;

	public String getFullPath() {
		return path + '/' + name;
	}
}
