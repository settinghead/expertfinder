import java.util.*;
import java.io.*;

public class App {
	private static Hashtable mapTable;
	private static Hashtable idfTable;
	private static Sim[] simValues;
	private static int numResearchers;

	public static void main(String[] args) throws Exception {
		mapTable = new Hashtable();
		idfTable = new Hashtable();
		mapNameDir(args[0]);
		idf(args[1]);
		Tfidf t = new Tfidf(idfTable);
		simValues = new Sim[numResearchers];
		t.bagOfWordsQuery(args[2]);
		//Runtime.getRuntime().exec("echo pavan");
		int count = 0;
		Scanner s = new Scanner(new File(args[0]));
		while (s.hasNextLine()) {
			String dir = s.nextLine().trim().split(" ")[0];
			t.processResearcher(dir);
			double sim = t.computeSim();
			String name = (String) mapTable.get(dir);
			simValues[count++] = new Sim(name, sim);
		}
		s.close();

		if (count < numResearchers)
			numResearchers = count;
		// sort
		Heapsort.sort(simValues, numResearchers);

		if (args.length == 3)
			display(numResearchers);
		else 
		{
			try {
			if (args[3].equals("num"))
				display(Integer.parseInt(args[4]));
			else
				display1(Double.parseDouble(args[4]));
			}
			catch (Exception e) {System.out.println("Please enter a valid value");}
		}
	}

	public static void mapNameDir(String fileName) {
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		}
		catch (IOException e) {System.out.println("File " + fileName + " not found");}
		while (s.hasNextLine()) {
			String line;
			do {
				line = s.nextLine().trim();
			} while (line.length() < 2);
			String[] tokens = line.split(" ");
			if (tokens.length != 2) {
				System.out.println("Error: " + fileName + " not "
						+ "in required format.");
				System.exit(0);
			}
			mapTable.put(tokens[0], tokens[1]);// check this
		}
	}

	public static void idf(String fileName) {
		Scanner s = null;
		try {
			s = new Scanner(new File(fileName));
		}
		catch (IOException e) {System.out.println("File " + fileName + " not found");}
		numResearchers = Integer.parseInt(s.nextLine().trim());										 
		double N = numResearchers;
		while (s.hasNextLine()) {
			String line;
			do {
				line = s.nextLine().trim();

			} while (line.length() < 2);
			String[] tokens = line.split(" ");
			if (tokens.length != 2) {
				System.out.println("Error: " + fileName + " not "
						+ "in required format.");
				System.exit(0);
			}
			int count = Integer.parseInt(tokens[1]);
			double frac = N / count;
			double val = Math.log(frac) / Math.log(2.0);
			idfTable.put(tokens[0], new Doub(val));
		}
		s.close();
	}

	public static void display(int num) {
		if (num > numResearchers) {
			System.out.println("You wanted the top " + num + " researchers. "
					+ "But, there are only " + numResearchers
					+ " researchers in" + " our database.");
			num = numResearchers;
		}
		System.out.println("Top " + num + " researchers to answer the query "
				+ "and their authority scores are:");
		for (int i = 0; i < num; i++)
			System.out.println(simValues[i].name + "\t" + simValues[i].auth);
	}

	public static void display1(double cutoff) {
		System.out.println("The researchers with authority score greater than "
				+ cutoff + " to answer the query are:");
		for (int i = 0; i < numResearchers; i++) {
			if (simValues[i].auth <= cutoff)
				break;
			System.out.println(simValues[i].name + "\t" + simValues[i].auth);
		}

	}
}
