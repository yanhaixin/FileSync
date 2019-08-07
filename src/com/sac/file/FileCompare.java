package com.sac.file;

import java.util.ArrayList;

public class FileCompare {
	public static ArrayList<DataFile> compareAndGet(FileCollection comA, FileCollection comB) {
		ArrayList<DataFile> rvalue = new ArrayList<DataFile>();
		for (DataFile file : comA.fileMap.values()) {
			if (!comB.fileMap.containsKey(file.name)) {
				rvalue.add(file);
			}
		}
		return rvalue;
	}
}
