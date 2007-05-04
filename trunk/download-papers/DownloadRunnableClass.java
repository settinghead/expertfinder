public class DownloadRunnableClass implements Runnable {
	String url;

	String location;

	public DownloadRunnableClass(String url, String location) {
		this.url = url;
		this.location = location;
	}

	public void run() {
//		FileDownload dl = new FileDownload();
//		dl.download(url, location);
		System.out.println(url);
		//PDFParser.pdfToText(url, System.out);
	}
}
