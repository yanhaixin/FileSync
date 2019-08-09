package com.sac.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.sac.common.Log;

public class Utility {
	private static final String[] unit = { "KB", "MB", "GB", "TB" };

	public static FileCollection readFileList(String path) {
		FileCollection rval = new FileCollection();
		try (Stream<Path> walk = Files.walk(Paths.get(path))) {

			Stream<DataFile> result = walk.filter(Files::isRegularFile).map(x -> new DataFile(x.getParent().toString(),
					x.getFileName().toString(), x.toFile().length(), x.toFile().lastModified()));
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

	public static String getSizeString(double size) {
		int step = 0;
		size /= 1024;
		while (size > 1024 && step < 3) {
			size /= 1024;
			step++;
		}
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(size) + " " + unit[step];
	}
}
