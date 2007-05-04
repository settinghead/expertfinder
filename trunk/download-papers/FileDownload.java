import java.io.*;
import java.net.*;

/*
 * Command line program to download data from URLs and save
 * it to local files. Run like this:
 * java FileDownload http://schmidt.devlib.org/java/file-download.html
 * @author Marco Schmidt
 */
public class FileDownload {
	public static void download(String address, String localFileName) {
		OutputStream out = null;
		URLConnection conn = null;
		InputStream  in = null;
		try {
			URL url = new URL(address);
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			out = new BufferedOutputStream(
					new FileOutputStream(localFileName));
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			System.out.println(localFileName + "\t" + numWritten);
		} catch (Exception exception) {
			exception.printStackTrace();
			
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
	}
	
	public static String downloadText(String address, int sizeLimit)
	{
		BufferedReader in = null;
		StringWriter out = null;
		URLConnection conn = null;
		String result="";
		try {
			URL url = new URL(address);
			conn = url.openConnection();
			 in = new BufferedReader(new InputStreamReader(url
			 .openStream()));
			char[] buffer = new char[1024];
			int numRead;
			long numWritten = 0;
			out = new StringWriter();
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
				if(numWritten>=sizeLimit) break;
			}
			out.flush();
			result = out.getBuffer().toString();
		} catch (Exception exception) {
			//exception.printStackTrace();
			
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
		return result;
	}
	
	public static void download(String address) {
		
		int lastSlashIndex = address.lastIndexOf('/');
		if (lastSlashIndex >= 0 &&
		    lastSlashIndex < address.length() - 1) {
			download(address, address.substring(lastSlashIndex + 1));
		} else {
			System.err.println("Could not figure out local file name for " +
				address);
		}
	}
}