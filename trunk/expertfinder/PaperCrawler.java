import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import org.htmlparser.*;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.*;
import org.htmlparser.util.*;
import java.net.*;
import java.util.regex.*;

public class PaperCrawler {

	Writer WRITER = new OutputStreamWriter(System.out);

	public boolean INCLUDE_HTML_FILES = true;

	public boolean INCLUDE_PDF_FILES = true;

	public int MAX_LEVEL = 3;

	public int NUMBER_OF_THREADS = 10;

	public boolean DEBUG_INFO = true;

	public Writer getWriter() {
		return WRITER;
	}

	public void setWriter(Writer writer) {
		this.WRITER = writer;
	}

	String currentSystemPath = "./";

	ArrayList<String> urlList = new ArrayList<String>();

	ArrayList<String> referringURLPath = new ArrayList<String>();

	ArrayList<Integer> urlLevels = new ArrayList<Integer>();

	Hashtable<String, Integer> history = new Hashtable<String, Integer>();

	DownloadWorkQueue downloadQueue = new DownloadWorkQueue(NUMBER_OF_THREADS,
			WRITER);

	public static void main(String args[]) {
		// TODO:resolve parameters
		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
			// switches
			case '-':
				break;
			default:
				break;
			}
		}
		PaperCrawler pw = new PaperCrawler();
		pw.crawl(args[0]);
	}

	public void crawl(String startURL) {

		// TODO: remove these after debugging
		ArrayList<String> urlList = this.urlList;
		ArrayList<Integer> urlLevels = this.urlLevels;
		Hashtable<String, Integer> history = this.history;
		try {
			// add the starting URL
			// urlList.add(getWebPage((new BufferedReader(
			// new InputStreamReader(System.in))).readLine()));
			urlList.add(startURL);
			referringURLPath.add(startURL);
			urlLevels.add(new Integer(0));
			history.put(startURL, new Integer(0));
		} catch (Exception ex) {
			printException(ex);
		}
		try {
			while (urlList.size() > 0) {
				// check if the address has been visited
				// visitNode(getAllLinkNodes(urlList.remove(0)), startURL,
				// startURL, urlLevels.remove(0).intValue());
				String path = urlList.remove(0);
				int level = urlLevels.remove(0).intValue();
				if (level == 0) {
					if (INCLUDE_PDF_FILES && isPdfURL(path)) {
						if (DEBUG_INFO)
							System.err.println(path
									+ " added to download queue.");
						downloadURL(path);
					} else if (INCLUDE_HTML_FILES)
						downloadURL(path);
				}
				visitLink(getLinks(path), startURL, referringURLPath.remove(0),
						level);
				debugPrintHistory();
			}
		} catch (Exception ex) {
			printException(ex);
		}

		downloadQueue.putAStop();

		 while (!downloadQueue.allThreadsStopped()) {
		 try {
		 Thread.sleep(100);
		 } catch (InterruptedException ex) {
		 }
		 }
		 ;
		//
		// try {
		// OUT.close();
		// } catch (IOException ex) {
		// printException(ex);
		// }
	}

	// static void visitNode(NodeList list, String startURL, String currentURL,
	// int level) {
	// Tag tag;
	// Node node;
	// SimpleNodeIterator iterator = list.elements();
	// while (iterator.hasMoreNodes()) {
	// node = iterator.nextNode();
	// if (node instanceof Tag) {
	// tag = (Tag) node;
	// // found a hyperlink
	// if (true
	// // tag.getTagName().toLowerCase().trim() == "a"
	// ) {
	// String path = "";
	//
	// URL current;
	// try {
	// current = new URL(new URL(getFilePath(currentURL)), tag
	// .getAttribute("href"));
	// path = removePositionTag(current.toURI().toString());
	// } catch (URISyntaxException ex) {
	// printException(ex);
	// } catch (MalformedURLException ex) {
	// printException(ex);
	// }
	// if (history.get(path.trim().toLowerCase()) == null) {
	// if (isPdfURL(path)) {
	// downloadURL(path);
	// } else if (withinScope(startURL, getFilePath(path))
	// && level < maxLevel) {
	// urlList.add(path);
	// urlLevels.add(new Integer(level + 1));
	// }
	// history.put(path.trim().toLowerCase(), new Integer(
	// level + 1));
	// }
	// }
	// }
	// }
	// }

	void visitLink(String[] list, String startURL, String referringURL,
			int level) {

		for (int i = 0; i < list.length; i++) {

			String path = "";

			URL current;
			try {
				current = new URL(new URL(getFilePath(referringURL)), list[i]);
				path = removePositionTag(current.toString());
			}
			// catch (URISyntaxException ex) {
			// printException(ex);
			// }
			catch (MalformedURLException ex) {
				printException(ex);
			}
			if (history.get(path.trim().toLowerCase()) == null) {
				if (INCLUDE_PDF_FILES && isPdfURL(path)) {
					if (DEBUG_INFO)
						System.err.println(path + " added to download queue.");
					downloadURL(path);
				} else if (withinScope(startURL, getFilePath(path))
						&& level < MAX_LEVEL) {
					if (DEBUG_INFO)
						System.err.println(path + " added to crawl list.");
					urlList.add(path);
					urlLevels.add(new Integer(level + 1));
					referringURLPath.add(path);

					if (INCLUDE_HTML_FILES)
						downloadURL(path);
				}
				history.put(path.trim().toLowerCase(), new Integer(level + 1));
			}
		}
	}

	String removePositionTag(String path) {
		int pos = path.indexOf('#');
		if (pos > 0)
			return path.substring(0, pos);
		else
			return path;

	}

	// static NodeList getAllLinkNodes(String url) {
	//
	// Parser parser;
	// NodeFilter filter;
	// NodeList list = null;
	// filter = new NodeClassFilter(LinkTag.class);
	// try {
	// parser = Parser.createParser(
	// // downloadHTML
	// (url), null);
	// list = parser.extractAllNodesThatMatch(filter);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return list;
	// }

	String[] getLinks(String url) {
		if (DEBUG_INFO)
			System.err.println("Analysing " + url + ".");
		ArrayList<String> list = new ArrayList<String>();
		String html = downloadHTML(url);

		Pattern pattern = Pattern
				.compile("<[aA].+[Hh][Rr][Ee][Ff]=\"?(.+?)[\" >]");
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			list.add(matcher.group(1));
		}

		String[] dummy = new String[0];
		return list.toArray(dummy);
	}

	String downloadHTML(String urlAddress) {
		try {
			return FileDownload.downloadText(urlAddress, 512000);
		} catch (Exception ex) {
			return "";
		}
	}

	boolean endWithForwardSlash(String url) {
		if (url == null || url.length() == 0)
			return false;
		try {
			if (url.charAt(url.length() - 1) == '/')
				return true;
			else
				return false;
		} catch (Exception ex) {
			printException(ex);
			return false;
		}
	}

	boolean withinScope(String startURL, String currentURL) {
		try {
			// remove the ending '/'
			if (endWithForwardSlash(startURL))
				startURL = startURL.substring(0, startURL.length() - 1);
			if (endWithForwardSlash(currentURL))
				currentURL = currentURL.substring(0, currentURL.length() - 1);
			if (currentURL.length() >= startURL.length()
					&& currentURL.substring(0, startURL.length()).equals(
							startURL))
				return true;
			else
				return false;
		} catch (Exception ex) {
			printException(ex);
			return false;
		}
	}

	static void printException(Exception ex) {
		// System.err.println(ex.getMessage());
		// System.err.println(ex.getStackTrace());
		// System.err.println(ex.getCause());
	}

	String getFilePath(String url) {
		// "http://abc.edu/hello/"
		if (endWithForwardSlash(url))
			return url;
		// "http://abc.edu/hello/ac.pdf" -->"http://abc.edu/hello/"
		else {
			if (FileDownload.checkIfURLExists(url + "/"))
				return url + "/";
			else
				return url.substring(0, url.lastIndexOf("/") + 1);
		}
	}

	public static boolean isPdfURL(String url) {
		try {
			url = url.trim().toLowerCase();
			String suffix = url.substring(url.length() - 4, url.length());
			if (suffix.equals(".pdf"))
				return true;
			else
				return false;
		} catch (Exception ex) {
			printException(ex);
			return false;
		}
	}

	void downloadURL(String urlAddress) {
		String fileName = urlAddress.replace('/', '_').replace('\\', '_')
				.replace('?', '_').replace(':', '_');
		// try {
		// URL url = new URL(urlAddress);
		// char[] data = new char[1024];
		// BufferedReader in = new BufferedReader(new InputStreamReader(url
		// .openStream()));
		// // FileReader in = new FileReader(new File(urlAddress));
		// BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
		// new FileOutputStream(
		// new File(currentSystemPath + fileName), false)));
		// int readBytes;
		// while ((readBytes = in.read(data)) != -1) {
		// out.write(data);
		// }
		//
		// out.close();
		// in.close();
		// } catch (FileNotFoundException e) {
		// System.err.println("FileCopy: " + e);
		// } catch (IOException e) {
		// System.err.println("FileCopy: " + e);
		// }

		downloadQueue.addToQueue(urlAddress, currentSystemPath + fileName,
				this.WRITER);

	}

	void debugPrintHistory() {
		// Object[] keys = history.keySet().toArray();
		// for (int i = 0; i < urlList.size(); i++)
		// System.out.println(urlList.indexOf(i))
		// ;
	}

	String getWebPage(String pageURL) {
		String str = "";
		try {
			URL url = new URL(pageURL);
			URLConnection conn = url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				str += "\r\n" + line;
			}
			rd.close();
		} catch (Exception ex) {
			printException(ex);
		}
		return str;
	}
}
