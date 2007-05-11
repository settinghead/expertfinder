import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class IntelligentURLSeeker {

	static int NUM_URLS = 3;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		ArrayList<String> names = new ArrayList<String>();
		while (sc.hasNextLine())
			names.add(sc.nextLine());
		String[] namesArray = (String[]) names.toArray(new String[0]);
		sc.close();
		String[] addresses = seekHomePages(namesArray);
		for (int i = 0; i < addresses.length; i++)
			System.out.println(addresses[i]);
	}

	public static String[] seekHomePages(String[] names) throws Exception {
		(new File("./tmp")).mkdir();
		FileWriter writer = new FileWriter(new File(
				"./tmp/tmp_raw_intelligentURL.txt"), false);
		String[][] urls = new String[names.length][];
		DownloadWorkQueue queue = new DownloadWorkQueue(10, writer);

		for (int i = 0; i < names.length; i++) {
			// retrieve URLs from search engine API
			urls[i] = NameToURL.getAddresses(names[i], NUM_URLS, null);
			for (int j = 0; j < urls[i].length; j++) {
				if (urls[i][j] != null && !PaperCrawler.isPdfURL(urls[i][j])) {
					PaperCrawler crawler = new PaperCrawler();
					crawler.DEBUG_INFO = true;
					crawler.INCLUDE_HTML_FILES = true;
					crawler.INCLUDE_PDF_FILES = true;
					crawler.MAX_LEVEL = 0;
					crawler.WRITER = new FileWriter("./tmp/tmp_" + i + "-" + j
							+ "_raw.txt", false);
					crawler.crawl(urls[i][j]);
					crawler.WRITER.flush();
					crawler.WRITER.close();
					ProfileBuilder.main(new String[] {
							"./tmp/tmp_" + i + "-" + j + "_raw.txt",
							"./tmp/tmp_" + i + "-" + j + "_profile.txt" });
					// write to collective profile
					FileReader reader = new FileReader("./tmp/tmp_" + i + "-"
							+ j + "_raw.txt");
					int c = 0;
					while ((c = reader.read()) > 0)
						writer.write(c);
				} else {
					urls[i][j] = null;
				}
			}
		}
		writer.close();

		// build profile & idf files
		ProfileBuilder.main(new String[] { "./tmp/tmp_raw_intelligentURL.txt",
				"./tmp/tmp_profile_intelligentURL.txt" });

		FileWriter mapWriter = new FileWriter("./tmp/tmp_map.txt");
		mapWriter.write("./tmp/tmp_profile_intelligentURL.txt huge");
		mapWriter.flush();
		mapWriter.close();

		WordFreq.main(new String[] { "./tmp/tmp_map.txt",
				"./tmp/tmp_idf_intelligentURL.txt" });

		// now compare and pick the best matching address for each person

		String[] results = new String[names.length];

		for (int i = 0; i < names.length; i++) {
			double maxSim = Double.NEGATIVE_INFINITY;
			int maxJ = -1;
			for (int j = 0; j < urls[i].length; j++) {
				if (urls[i][j] != null) {
					Sim[] sims = App.query(new String[] { "./tmp/tmp_map.txt",
							"./tmp/tmp_idf_intelligentURL.txt",
							"./tmp/tmp_" + i + "-" + j + "_profile.txt" });
					double similarity = 0;
					if (sims.length > 0)
						similarity = sims[0].auth;
					// find an address with larger similarity
					if (similarity > maxSim) {
						maxSim = similarity;
						maxJ = j;
					}
				}

			}
			results[i] = urls[i][maxJ];
		}
		return results;
	}
}
