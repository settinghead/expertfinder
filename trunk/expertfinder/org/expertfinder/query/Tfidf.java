package org.expertfinder.query;

import java.io.*;
import java.util.*;

public class Tfidf {
	private Hashtable queryTable;

	private Hashtable resTable;

	private Hashtable idfTable;

	public Tfidf(Hashtable idfTable) {
		this.idfTable = idfTable;
	}

	public void bagOfWordsQuery(String fileName) throws Exception {
		queryTable = new Hashtable();
		bagOfWordsToTable(fileName, queryTable);
	}

	private void bagOfWordsToTable(String fileName, Hashtable table)
			throws Exception {
		Scanner s = new Scanner(new File(fileName));
		double maxCount = Double.parseDouble(s.nextLine().trim());
		while (s.hasNextLine()) {
			String line = s.nextLine().trim();
			String[] tokens = line.split(" ");
			if (tokens.length != 2) {
				System.out.println("Error: query file not "
						+ "in required format.");
				System.exit(0);
			}
			Doub idfObj = (Doub) idfTable.get(tokens[0]);
			double idf = idfObj.value;
			double tf = Integer.parseInt(tokens[1]) / maxCount;
			table.put(tokens[0], new Doub(tf * idf));
		}
	}

	public void processResearcher(String path) throws Exception {
		resTable = new Hashtable();
		bagOfWordsToTable(path, resTable);
	}

	public double computeSim() {
		double modA = 0;
		double modB = 0;
		double dotProd = 0;
		for (Enumeration e = queryTable.keys(); e.hasMoreElements();) {
			String word = (String) e.nextElement();
			Doub obj = (Doub) queryTable.get(word);
			if (obj == null)
				continue;
			double a = obj.value;
			obj = (Doub) resTable.get(word);
			double b = 0;
			if (obj != null)
				b = obj.value;
			modA += a * a;
			dotProd += a * b;
		}
		modA = Math.sqrt(modA);

		for (Enumeration e = resTable.keys(); e.hasMoreElements();) {
			Doub obj = (Doub) resTable.get(e.nextElement());
			if (obj == null)
				continue;
			double b = obj.value;
			modB += b * b;
		}
		modB = Math.sqrt(modB);

		double sim = 0;
		if (modA != 0 && modB != 0) {
			sim = dotProd / (modA * modB);
		}
		sim = Math.round(sim * 1000) / 1000.0;
		return sim;
	}

}

class Int {
	public int value;

	public Int(int argVal) {
		value = argVal;
	}
}

class Doub {
	public double value;

	public Doub(double argVal) {
		value = argVal;
	}
}

class Sim {
	public String name;

	public double auth;

	public Sim(String a, double b) {
		name = a;
		auth = b;
	}
}
