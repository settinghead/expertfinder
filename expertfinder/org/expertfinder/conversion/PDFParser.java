package org.expertfinder.conversion;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.expertfinder.crawl.FileDownload;
import org.expertfinder.tokenization.Tokenizer;
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
	public static void pdfToTextWithExternalUtility(String url,
			OutputStream stream) {
		(new File("./tmp")).mkdir();
		FileDownload.download(url, "./tmp/tmp.pdf");
		try {
			Runtime.getRuntime().exec(
					"pdftotext " + "./tmp/tmp.pdf ./tmp/tmp.txt");
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
		StringWriter writer = null;
		try {
			writer = new StringWriter();
			Scanner sc = new Scanner(new FileInputStream("./tmp/tmp.txt"));
			while (sc.hasNextLine())
				writer.write(sc.nextLine());
			writer.flush();
			Tokenizer.tokenize(writer.getBuffer().toString(), stream);
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static void pdfToText(String url, OutputStream stream) {
		PDDocument document = null;
		StringWriter writer = null;
		try {
			if (!FileDownload.checkIfURLOversized(url, 1024 * 1024 * 5)) {
				document = PDDocument.load(new URL(url));
				PDFTextStripper stripper = new PDFTextStripper();
				writer = new StringWriter();
				// writer.append(url + "\r\n");
				stripper.writeText(document, writer);
				writer.flush();
				// System.out.print(writer.toString());
				Tokenizer.tokenize(writer.getBuffer().toString(), stream);
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		} finally {
			if (document != null)
				try {
					document.close();
				} catch (IOException ex) {
				}

			if (writer != null)
				try {
					writer.close();
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
