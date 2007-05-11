
import java.io.*;
import java.util.Scanner;

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
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		storeParameters(args);
		StringWriter sw = new StringWriter();
		switch (INPUT_FORMAT) {
		case WebURL:
			// the url is a pdf file
			if (SOURCE_NAME.endsWith(".pdf")) {
				PDFParser.pdfToTokenizedText(SOURCE_NAME,
						PDFParser.SourceType.WebAddress, sw);
			} else {
				Tokenizer.tokenize(FileDownload.downloadText(SOURCE_NAME,
						1024 * 1024 * 5), sw);
			}
			break;
		case PDFFile:
			PDFParser.pdfToTokenizedText(SOURCE_NAME,
					PDFParser.SourceType.LocalFile, sw);
			break;
		case HTMLFile:
			// TO DO!!!!!!!!!!!!!!!!!
			break;
		case ConsoleInput:
		default:
			Scanner sc = new Scanner(System.in);
			StringBuffer sb = new StringBuffer();
			while (sc.hasNextLine())
				sb.append((sc.nextLine()));
			Tokenizer.tokenize(sb.toString(), sw);
			break;
		}
		Reader reader = new StringReader(sw.toString());
		Writer writer = new OutputStreamWriter(System.out);
		Controller.queryResult(reader, writer);
		reader.close();
		writer.close();
		sw.close();
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
