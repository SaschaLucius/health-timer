package datameer.health.frontend;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Random;

public class ImageHelper {
	public static String getImage(String filter) {
		FilenameFilter filenameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(filter);
			}
		};
		URL resource = ImageHelper.class.getClassLoader().getResource("img/brain.jpg");
		File file = new File(resource.getFile());
		file = file.getParentFile();
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
