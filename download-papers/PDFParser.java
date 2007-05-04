import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

public class PDFParser {
	public static void pdfToText(String url, OutputStream stream) {
		try {
			PDDocument document = PDDocument.load(new URL(url));
			PDFTextStripper stripper = new PDFTextStripper();
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			stripper.writeText(document, writer);
//			document.close();
//			writer.close();
		} catch (IOException ex) {
			
		}
	}
}
