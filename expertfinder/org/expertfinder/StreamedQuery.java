package org.expertfinder;

import org.expertfinder.query.*;
import java.io.*;

public class StreamedQuery {

	static final String IDF_PATH_SWITCH = "-idfpath";

	static final String PRINT_HELP_SWITCH = "-help";

	static String IDF_PATH = "./idf/";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Controller.queryResult(new InputStreamReader(System.in),
				new OutputStreamWriter(System.out));
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
		System.out.println("Usage: StreamedQuery [-idfepath path]");
	}

}
