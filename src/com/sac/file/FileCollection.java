package com.sac.file;

import java.util.HashMap;

public class FileCollection {
	protected HashMap<String, DataFile> fileMap = new HashMap<String, DataFile>();

	public void pushDataFile(DataFile file) {
		fileMap.put(file.name, file);
	}
}
