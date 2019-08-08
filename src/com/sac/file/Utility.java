package com.sac.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.sac.common.Log;

public class Utility {
	public static FileCollection readFileList(String path) {
		FileCollection rval = new FileCollection();
		try (Stream<Path> walk = Files.walk(Paths.get(path))) {

			Stream<DataFile> result = walk.filter(Files::isRegularFile)
					.map(x -> new DataFile(x.getParent().toString(), x.getFileName().toString()));
			result.forEach(x -> rval.put(x));
		} catch (IOException e) {
			Log.logger.error(e.getMessage());
		}
		return rval;
	}

	public static ArrayList<DataFile> compareAndGet(FileCollection remote, FileCollection local) {
		ArrayList<DataFile> rvalue = new ArrayList<DataFile>();
		remote.forEach(file -> {
			if (!local.fileMap.containsKey(file.name)) {
				rvalue.add(file);
			}
		});
		return rvalue;
	}
}
