
import java.io.*;
import java.util.Scanner;

public class Controller {

	static String PROFILE_PATH = "./profiles/";

	static Writer QUERY_WRITER = new OutputStreamWriter(System.out);

	public static void main(String[] args) throws Exception {
		//build();
		buildIdf();
		//query();
	}

	static void build() {
		String[] names = new String[] { 
//				"Dieter van Melkebeek",
//				"Robert R. Meyer", "Jerry Zhu", "Anhai Doan", "Charles Dyer",
//				"David J. DeWitt", "Barton P. Miller", "Amos Ron",
//				"Karthikeyan Sankaralingam", "Marvin Solomon",
//				"Mary K. Vernon", "Michael M. Swift", "Gurindar S. Sohi",
//				"David A. Wood", "Stephen J. Wright",
//				// " Jude Shavlik",
//				"Raghu Ramakrishnan", "Cristian Estan", "Susan B. Horwitz",
				"Somesh Jha" };
		buildProfiles(names);
	}

	static void query() throws Exception {
		queryResult("operating system kernel", QUERY_WRITER);
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

					PaperCrawler crawler = new PaperCrawler();
					FileWriter writer = new FileWriter(new File(rawFilePath),
							false);
					(crawler).setWriter(writer);

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

	public static void queryResult(Reader reader, Writer writer)
			throws Exception {
		Scanner sc = new Scanner(reader);

		StringBuffer sb = new StringBuffer();

		while (sc.hasNextLine())
			sb.append(sc.nextLine());

		queryResult(sb.toString(), writer);

		sc.close();
	}

	public static void queryResult(String query, Writer writer)
			throws Exception {
		(new File(PROFILE_PATH + "query")).mkdir();

		String mapFile = PROFILE_PATH + "map.txt";
		String idfFile = PROFILE_PATH + "query/idf.txt";
		String queryFile = PROFILE_PATH + "query/query.txt";
		String queryProfileFile = PROFILE_PATH + "query/profile_query.txt";

		// write to the query file
		FileWriter fWriter;
		(fWriter = new FileWriter(new File(queryFile))).write(query);
		fWriter.close();

		// build profile for the query
		ProfileBuilder.main(new String[] { queryFile, queryProfileFile });
		//App.setWriter(writer);
		App.main(new String[] { mapFile, idfFile, queryProfileFile });
		//writer.close();
	}
}
