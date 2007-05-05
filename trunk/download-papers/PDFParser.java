import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

//import org.pdfbox.pdmodel.PDDocument;
//import org.pdfbox.util.PDFTextStripper;

import org.jpedal.*;
import org.jpedal.exception.*;
import org.jpedal.grouping.PdfGroupingAlgorithms;
import org.jpedal.objects.PdfPageData;

public class PDFParser {
	public static void pdfToText(String url, OutputStream stream) {
		// try {
		// PDDocument document = PDDocument.load(new URL(url));
		// PDFTextStripper stripper = new PDFTextStripper();
		// OutputStreamWriter writer = new OutputStreamWriter(stream);
		// stripper.writeText(document, writer);
		// // document.close();
		// // writer.close();
		// } catch (IOException ex) {
		//			
		// }
		OutputStreamWriter writer = new OutputStreamWriter(stream);
		PdfDecoder dec = new PdfDecoder(false);
		PdfDecoder.useTextExtraction();
		dec.setExtractionMode(PdfDecoder.TEXT);
		dec.init(true);
		try {
			dec.openPdfFileFromURL(url);
			int numPages = dec.getPageCount();
			for (int i = 1; i <= numPages; i++) {
				try {
					dec.decodePage(i);
					PdfGroupingAlgorithms currentGrouping = dec
							.getGroupingObject();

					/**
					 * use whole page size for demo - get data from PageData
					 * object
					 */
					PdfPageData currentPageData = dec.getPdfPageData();

					int x1, y1, x2, y2;

					x1 = currentPageData.getMediaBoxX(i);
					x2 = currentPageData.getMediaBoxWidth(i) + x1;

					y2 = currentPageData.getMediaBoxY(i);
					y1 = currentPageData.getMediaBoxHeight(i) + y2;
					String text = currentGrouping.extractTextInRectangle(x1, y1,
							x2, y2, i, false, true);
					writer.write(text);
					writer.flush();
				} catch (Exception ex) {
					System.err.println(ex.getMessage());
				}
			}
//			try {
//				writer.close();
//			} catch (IOException ex) {
//				System.err.println(ex.getMessage());
//			}
			dec.closePdfFile();

		} catch (PdfException ex) {
			System.err.println(ex.getMessage());
		}
	}
}
