package com.sac.file;

import java.util.HashMap;
import java.util.function.Consumer;

public class FileCollection {
	protected HashMap<String, DataFile> fileMap = new HashMap<String, DataFile>();

	public void put(DataFile file) {
		fileMap.put(file.name, file);
	}

	public void forEach(Consumer<DataFile> consumer) {
		for (DataFile file : fileMap.values()) {
			consumer.accept(file);
		}
	}
}
