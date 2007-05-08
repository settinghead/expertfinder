package org.expertfinder;

import java.io.*;
import org.expertfinder.crawl.*;
import org.expertfinder.profilebuilder.*;
import org.expertfinder.query.*;

public class Controller {

	static String PROFILE_PATH = "./profiles/";

	public static void main(String[] args) throws Exception {
		// build();
		//buildIdf();
		query();
	}

	static void build() {
		String[] names = new String[] { "Dieter van Melkebeek",
				"Robert R. Meyer", "Jerry Zhu", "Anhai Doan", "Charles Dyer",
				"David J. DeWitt", "Barton P. Miller", "Amos Ron",
				"Karthikeyan Sankaralingam", "Marvin Solomon",
				"Mary K. Vernon", "Michael M. Swift", "Gurindar S. Sohi",
				"David A. Wood", "Stephen J. Wright",
				// " Jude Shavlik",
				"Raghu Ramakrishnan", "Cristian Estan", "Susan B. Horwitz",
				"Somesh Jha" };
		buildProfiles(names);
	}

	static void query() throws Exception {
		queryResult("operating system kernel", null, null);
	}

	public static void buildProfiles(String[] names) {
		(new File(PROFILE_PATH)).mkdir();
		try {
			FileWriter mapWriter = new FileWriter(new File(PROFILE_PATH
					+ "map.txt"), false);

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

					String[] addresses = NameToURL.getAddresses(names[i], 1,
							null);
					if (addresses.length > 0) {
						String address = addresses[0];
						crawler.crawl(address);
						ProfileBuilder.main(new String[] { rawFilePath,
								profileFilePath });
					}
					mapWriter.write(profileFilePath + " "
							+ names[i].replaceAll(" ", "_") + "\n");
				} catch (FileNotFoundException ex) {
					System.err.println(ex.getMessage());
				}
			}
			mapWriter.flush();
			mapWriter.close();
		} catch (IOException ex) {
		}
	}

	static void buildIdf() throws Exception {
		String mapFile = PROFILE_PATH + "map.txt";
		String idfFile = PROFILE_PATH + "query/idf.txt";
		WordFreq.main(new String[] { mapFile, idfFile });
	}

	public static void queryResult(String query, String[] nameResult,
			double[] rankValueResult) throws Exception {
		(new File(PROFILE_PATH + "query")).mkdir();

		String mapFile = PROFILE_PATH + "map.txt";
		String idfFile = PROFILE_PATH + "query/idf.txt";
		String queryFile = PROFILE_PATH + "query/query.txt";
		String queryProfileFile = PROFILE_PATH + "query/profile_query.txt";

		ProfileBuilder.main(new String[] { queryFile, queryProfileFile });

		FileWriter writer;
		(writer = new FileWriter(new File(queryFile))).write(query);
		writer.close();

		App.main(new String[] { mapFile, idfFile, queryProfileFile });

	}
}
