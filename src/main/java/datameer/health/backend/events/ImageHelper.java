package datameer.health.backend.events;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;

public class ImageHelper {
	public static String getImage(String filter) {
		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(filter);
			}
		};
		File file = new File("src/main/resources/img/");
		Random r = new Random();
		int Low = 0;
		File[] listFiles = file.listFiles(filenameFilter);
		if (listFiles != null) {
			int High = listFiles.length - 1;
			int Result = 0;
			if (High > 0) {
				Result = r.nextInt(High - Low) + Low;
			}
			return "img/" + listFiles[Result].getName();
		} else {
			return "http://www.omsakthiamma.org/images/404.png";
		}
	}
}
