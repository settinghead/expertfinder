package org.expertfinder;

import java.io.*;
import org.expertfinder.crawl.*;
import org.expertfinder.profilebuilder.*;
import org.expertfinder.profilequery.*;

public class Controller {

	static String PROFILE_PATH = "./profiles/";

	public static void main(String[] args) {
		String[] names = new String[] {
//				"Jerry Zhu",
//				"Anhai Doan", 
				"Charles Dyer", 
				"David J. DeWitt",
				" Jude Shavlik" };
		buildProfiles(names);
	}

	public static void buildProfiles(String[] names) {
		(new File(PROFILE_PATH)).mkdir();
		for (int i = 0; i < names.length; i++) {
			try {
				String rawFileName = names[i].replace(" ", "");

				String rawFilePath = PROFILE_PATH + rawFileName + ".txt";
				String profileFilePath = PROFILE_PATH + "profile_"
						+ rawFileName + ".txt";

				FileOutputStream out = new FileOutputStream(new File(
						rawFilePath), false);
				PaperCrawler crawler = new PaperCrawler();
				(crawler).setOutputStream(out);

				String[] addresses = NameToURL.getAddresses(names[i], 1, null);
				if (addresses.length > 0) {
					String address = addresses[0];
					crawler.crawl(address);
					ProfileBuilder.main(new String[] { rawFilePath,
							profileFilePath });
				}
			} catch (FileNotFoundException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
}
