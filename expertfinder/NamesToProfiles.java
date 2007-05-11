import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class NamesToProfiles {

	/**
	 * @param args
	 */

	static final String PROFILE_PATH_SWITCH = "-profilepath";

	static final String PRINT_HELP_SWITCH = "-help";

	static final String INTELLIGENT_URL_SWITCH = "-intelligenturl";

	static String PROFILE_PATH = "./";

	static boolean INTELLIGENT_URL = true;

	public static void main(String[] args) throws Exception {

		storeParameters(args);

		// Set profile path
		Controller.PROFILE_PATH = PROFILE_PATH;

		Scanner sc = new Scanner(System.in);
		ArrayList<String> names = new ArrayList<String>();
		while (sc.hasNextLine())
			names.add(sc.nextLine());
		String[] namesArray = (String[]) names.toArray(new String[0]);
		// retrieve web addresses
		String[] addresses;

		if (INTELLIGENT_URL) {
			addresses = IntelligentURLSeeker.seekHomePages(namesArray);
		} else {
			addresses = new String[namesArray.length];
			for (int i = 0; i < namesArray.length; i++) {
				addresses[i] = NameToURL.getAddresses(namesArray[i], 1, null)[0];
			}
		}

		Controller.buildProfiles((String[]) names.toArray(new String[0]),
				addresses);
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
							PROFILE_PATH_SWITCH))
						PROFILE_PATH = args[++i].endsWith("/") ? args[i]
								: args[i] + "/"; // append forward slash if
					// necessary
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
		System.out.println("Usage: NamesToProfiles [-profilepath path]");
	}

}
