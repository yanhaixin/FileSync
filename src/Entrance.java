import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import com.sac.connection.FileSelector;
import com.sac.connection.SFTP;

public class Entrance {

	public static void main(String[] args) {

		SFTP sftp = new SFTP("172.18.7.105", "sac", "yanhaixin1980", 22);
		sftp.connect();
		try {
			ArrayList<LsEntry> filelist = sftp.ls("/home/sac/project", new FileSelector());
			for (LsEntry entry : filelist) {
				System.out.println(entry.getFilename());
				sftp.getAndSave("/home/sac/project/" + entry.getFilename(), "C:/test");
			}

		} catch (SftpException e) {
			e.printStackTrace();
		}
		sftp.disConnect();

		try (Stream<Path> walk = Files.walk(Paths.get("C:\\Users\\yanhaixin\\eclipse-workspace\\FileSync\\lib"))) {

			List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());

			result.forEach(System.out::println);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
