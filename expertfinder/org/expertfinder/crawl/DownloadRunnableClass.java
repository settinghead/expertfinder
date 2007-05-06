package org.expertfinder.crawl;

import java.io.*;

public class DownloadRunnableClass implements Runnable {

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
		PDFParser.pdfToText(url, OUT);
		// try
		// {
		// ExtractText.extract(new String[]{url,"-console"}, System.out);
		// }
		// catch (Exception ex){
		// System.err.println(ex.getMessage());
		// }
	}
}
