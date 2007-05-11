


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

//import org.jpedal.*;
//import org.jpedal.exception.*;
//import org.jpedal.grouping.PdfGroupingAlgorithms;
//import org.jpedal.objects.PdfPageData;
//import org.jpedal.utils.Strip;
//
//import java.util.Iterator;
//import java.util.List;

public class PDFParser {

	public enum SourceType {
		WebAddress, LocalFile
	};

	public static void pdfToTokenizedTextWithExternalDownloadUtility(String url,
			Writer writer) {
		(new File("./tmp")).mkdir();
		FileDownload.download(url, "./tmp/tmp.pdf");
		try {
			Runtime.getRuntime().exec(
					"pdftotext " + "./tmp/tmp.pdf ./tmp/tmp.txt");
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		StringWriter sWriter = null;
		try {
			sWriter = new StringWriter();
			Scanner sc = new Scanner(new FileInputStream("./tmp/tmp.txt"));
			while (sc.hasNextLine())
				sWriter.write(sc.nextLine());
			sWriter.flush();
			Tokenizer.tokenize(sWriter.getBuffer().toString(), writer);
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static void pdfToTokenizedText(String sourceName, SourceType type,
			Writer writer) {
		PDDocument document = null;
		StringWriter sWriter = null;
		try {
			if (!FileDownload.checkIfURLOversized(sourceName, 1024 * 1024 * 5)) {
				if (type == SourceType.WebAddress)
					document = PDDocument.load(new URL(sourceName));
				else if (type == SourceType.LocalFile)
					document = PDDocument.load(sourceName);
				PDFTextStripper stripper = new PDFTextStripper();
				sWriter = new StringWriter();
				// writer.append(url + "\r\n");
				stripper.writeText(document, sWriter);
				sWriter.flush();
				// System.out.print(writer.toString());
				Tokenizer.tokenize(sWriter.getBuffer().toString(), writer);
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
			if (document != null)
				try {
					document.close();
				} catch (IOException ex) {
				}

			if (sWriter != null)
				try {
					sWriter.close();
				} catch (IOException ex) {
				}

		}
		// OutputStreamWriter writer = new OutputStreamWriter(stream);
		//
		// PdfDecoder.useTextExtraction();
		//
		// PdfDecoder dec = new PdfDecoder(false);
		// dec.setExtractionMode(PdfDecoder.TEXT); // extract just text
		// dec.init(true);
		// // PdfDecoder.useTextExtraction();
		//
		// PdfGroupingAlgorithms.useUnrotatedCoords = false;
		//
		// try {
		//
		// dec.openPdfFileFromURL(url);
		// int numPages = dec.getPageCount();
		// for (int i = 1; i <= numPages; i++) {
		// try {
		// dec.decodePage(i);
		// PdfGroupingAlgorithms currentGrouping = dec
		// .getGroupingObject();
		// /**
		// * use whole page size for demo - get data from PageData
		// * object
		// */
		// PdfPageData currentPageData = dec.getPdfPageData();
		//
		// int x1, y1, x2, y2;
		//
		// x1 = currentPageData.getMediaBoxX(i);
		// x2 = currentPageData.getMediaBoxWidth(i) + x1;
		//
		// y2 = currentPageData.getMediaBoxY(i);
		// y1 = currentPageData.getMediaBoxHeight(i) + y2;
		// List words = currentGrouping.extractTextAsWordlist(
		// x1,
		// y1,
		// x2,
		// y2,
		// i,
		// false, true,"&:=()!;.,\\/\"\"\'\'");
		// Iterator wordIterator=words.iterator();
		// while(wordIterator.hasNext()){
		//							
		// String currentWord=(String) wordIterator.next();
		//							
		// /**remove the XML formatting if present - not needed for pure text*/
		// currentWord=Strip.convertToText(currentWord);
		//							
		// /**this could be inserting into a database instead*/
		// writer.write(currentWord+" ");
		//
		// }
		// writer.write("\n");
		// writer.flush();
		// } catch (Exception ex) {
		// System.err.println(ex.getMessage());
		// }
		// }
		// // try {
		// // writer.close();
		// // } catch (IOException ex) {
		// // System.err.println(ex.getMessage());
		// // }
		//
		// dec.closePdfFile();
		// } catch (PdfException ex) {
		//
		// // System.err.println(ex.getMessage());
		// dec.closePdfFile();
		// }

	}
}
