package org.expertfinder;

import java.io.*;
import org.expertfinder.crawler.conversion.*;

public class Query {

	static final String IDF_PATH_SWITCH = "-idfpath";

	static final String PRINT_HELP_SWITCH = "-help";

	static final String PDF_FILE_SWITCH = "-pdf";

	static final String HTML_FILE_SWITCH = "-html";

	static final String WEB_ADDRESS_SWITCH = "-webaddress";

	static final String KEYWORDS_SWITCH = "-keywords";

	static String IDF_PATH = "./idf/";

	static String SOURCE_NAME = null;

	enum InputFormat {
		PDFFile, HTMLFile, WebURL, ConsoleInput
	};

	static InputFormat INPUT_FORMAT = InputFormat.ConsoleInput;

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		storeParameters(args);
		Reader reader = null;
		Writer writer = new OutputStreamWriter(System.out); 
		switch (INPUT_FORMAT) {
		case WebURL:

			break;
		case PDFFile:
			StringWriter sw = new StringWriter();
			PDFParser.pdfToTokenizedText(SOURCE_NAME,
					PDFParser.SourceType.LocalFile, sw);
			sw.flush();
			reader = new StringReader(sw.toString());
			break;
		case HTMLFile:
			break;
		case ConsoleInput:
		default:
			break;
		}
		Controller.queryResult(reader, writer);
		reader.close();
		writer.close();
	}

	static void storeParameters(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				switch (args[i].toLowerCase().charAt(0)) {
				// switches
				case '-':
					if (args[i].trim().toLowerCase().equals(PRINT_HELP_SWITCH))
						printUsage();
					else if (args[i].trim().toLowerCase().equals(
							IDF_PATH_SWITCH))
						IDF_PATH = args[++i].endsWith("/") ? args[i] : args[i]
								+ "/"; // append forward slash if necessary
					else if (args[i].trim().toLowerCase().equals(
							WEB_ADDRESS_SWITCH)) {
						SOURCE_NAME = args[++i];
						INPUT_FORMAT = InputFormat.WebURL;
					} else if (args[i].trim().toLowerCase().equals(
							PDF_FILE_SWITCH)) {
						SOURCE_NAME = args[++i];
						INPUT_FORMAT = InputFormat.PDFFile;
					} else if (args[i].trim().toLowerCase().equals(
							HTML_FILE_SWITCH)) {
						SOURCE_NAME = args[++i];
						INPUT_FORMAT = InputFormat.HTMLFile;
					} else if (args[i].trim().toLowerCase().equals(
							KEYWORDS_SWITCH)) {
						INPUT_FORMAT = InputFormat.ConsoleInput;
					}
					break;
				default:
					break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Error: cannot resolve parameters.");
			printUsage();
		}
	}

	static void printUsage() {
		System.out
				.println("Usage: Query [-idfpath path] [[-webaddress url] | [-pdf filename] | [-html filename] | [-keywords]]");
	}

}
