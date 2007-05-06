package org.expertfinder.crawl;

import java.io.*;
import java.util.Hashtable;
import java.util.Scanner;

public class DownloadRunnableClass implements Runnable {

	static Hashtable<String, Integer> restrictedURLs = new Hashtable<String, Integer>();

	static {

		try {
			FileInputStream input = new FileInputStream(
					"restricted_connection_list.txt");
			Scanner sc = new Scanner(input).useDelimiter("[ \r\n]+");

			while (sc.hasNext()) {
				String address = sc.next();
				if (address.length() > 0)
					restrictedURLs.put(address.trim().toLowerCase(),
							new Integer(Integer.parseInt(sc.next().trim())));
			}
			sc.close();
		} catch (FileNotFoundException ex) {
			System.err.println("Warning: cannot find restricted URL list.");
		}
	}

	public synchronized static boolean getAddressPass(String address) {
		boolean result = true;
		synchronized (restrictedURLs) {
			for (int i = 0; i < address.length(); i++) {
				Integer count = null;
				if ((count = restrictedURLs.get(address.substring(0, i + 1))) != null) {
					// if there are still slots
					if (count.intValue() > 0) {
						restrictedURLs.remove(address.substring(0, i + 1));
						restrictedURLs.put(address.substring(0, i + 1),
								new Integer(count - 1));
						break;
					}
					// no more free slots available
					else {
						result = false;
					}

				}
			}
			restrictedURLs.notifyAll();
			return result;
		}
	}

	public synchronized static void releaseAddressPass(String address) {
		synchronized (restrictedURLs) {
			for (int i = 0; i < address.length(); i++) {
				Integer count = null;
				if ((count = restrictedURLs.get(address.substring(0, i + 1))) != null) {
					{
						restrictedURLs.remove(address.substring(0, i + 1));
						restrictedURLs.put(address.substring(0, i + 1),
								new Integer(count + 1));
						break;
					}
				}
			}
			restrictedURLs.notifyAll();
			return;
		}
	}

	private OutputStream OUT = System.out;

	public void setOutputStream(OutputStream out) {
		OUT = out;
	}

	public OutputStream getOutputStream() {
		return OUT;
	}

	String url;

	String location;

	public DownloadRunnableClass(String url, String location, OutputStream out) {
		this.url = url;
		this.location = location;
		this.OUT = out;
	}

	public void run() {
		// FileDownload dl = new FileDownload();
		// dl.download(url, location);
		// System.out.println(url);
		while (!getAddressPass(url))
			;
		PDFParser.pdfToText(url, OUT);
		releaseAddressPass(url);
		// try
		// {
		// ExtractText.extract(new String[]{url,"-console"}, System.out);
		// }
		// catch (Exception ex){
		// System.err.println(ex.getMessage());
		// }
	}
}
