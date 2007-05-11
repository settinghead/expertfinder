import java.io.*;

public class IntelligentURLSeeker {

	static int NUM_URLS = 10;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static String[] seekHomePages(String[] names) throws Exception {
		(new File("./tmp")).mkdir();
		FileWriter writer = new FileWriter(new File(
				"./tmp/tmp_raw_intelligentURL.txt"), false);
		String[][] urls = new String[names.length][];
		DownloadWorkQueue queue = new DownloadWorkQueue(10, writer);
		PaperCrawler crawler = new PaperCrawler();
		crawler.DEBUG_INFO = true;
		crawler.INCLUDE_HTML_FILES = true;
		crawler.INCLUDE_PDF_FILES = true;
		crawler.MAX_LEVEL = 1;

		for (int i = 0; i < names.length; i++) {
			// retrieve URLs from search engine API
			urls[i] = NameToURL.getAddresses(names[i], NUM_URLS, null);
			for (int j = 0; j < urls[i].length; j++) {
				if (urls[i][j] != null) {
					crawler.WRITER = new FileWriter("./tmp/tmp_" + i + "-" + j
							+ "_profile.txt", false);
					crawler.crawl(urls[i][j]);
					crawler.WRITER.close();
					// write to collective profile
					FileReader reader = new FileReader("./tmp/tmp_" + i + "-"
							+ j + "_profile.txt");
					int c = 0;
					while ((c = reader.read()) > 0)
						writer.write(c);
				}
			}
		}
		writer.close();

		// build profile & idf files
		ProfileBuilder.main(new String[] { "./tmp/tmp_raw_intelligentURL.txt",
				"./tmp/tmp_profile_intelligentURL.txt" });
		WordFreq.main(new String[] { "./tmp_profile_intelligentURL.txt",
				"./tmp/tmp_idf_intelligentURL.txt" });
		FileWriter mapWriter = new FileWriter("./tmp/tmp_map.txt");
		mapWriter.write("huge ./tmp_profile_intelligentURL.txt");
		mapWriter.flush();
		mapWriter.close();

		// now compare and pick the best matching address for each person

		String[] results = new String[names.length];

		for (int i = 0; i < names.length; i++) {
			double maxSim = Double.NEGATIVE_INFINITY;
			int maxJ = -1;
			for (int j = 0; j < urls[i].length; j++) {
				Sim[] sims = App
						.query(new String[] {
								"./tmp/tmp_map.txt",
								"./tmp/tmp_idf_intelligentURL.txt",
								j + " " + "./tmp/tmp_" + i + "-" + j
										+ "_profile.txt" });
				double similarity = 0;
				if (sims.length > 0)
					similarity = sims[0].auth;
				// find an address with larger similarity
				if (similarity > maxSim) {
					maxSim = similarity;
					maxJ = j;
				}

			}
			results[i] = urls[i][maxJ];
		}
		return results;
	}
}
