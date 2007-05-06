package org.expertfinder.profilebuilder;
/**
 * This file takes as input a file map.txt, which contains the mappings of 
 * each researcher's profile to the documents. 
 * @author Rokas
 *
 */


import java.util.*;
import java.io.*;
public class build_profile {
	
	public static final boolean DEBUG = true;
	
	public static void main(String[] args) {
		
		// check if input correct
		if (args.length < 2) {
			System.out.println("Too few arguemtns.");
			System.out.println("Usage: build_profile file_1 file_2 ... file_N output_file");
			System.exit(1);
		}
		
		// Generate BoW and write them to the given file
		generateWrite(args);
		
	}
	
	
	// take a string array containing file names (with appropriate direcotries)
	// generate a bag-of-word representation from all of them (except last)
	// Eventually write them to the file contained in the last element of the array
	private static void generateWrite(String[] data) {
		
		// a hashtable indexed by word storing the count of each word
		Hashtable<String,Integer> myTable = new Hashtable<String,Integer>();
		int highest=0;
		
		for (int i=0; i<data.length-1; i++) {
			try {
				Scanner inp = new Scanner(new FileInputStream(new File(data[i])));
				while (inp.hasNext()) {
					String word = inp.next();
					System.out.println(word);
					// if the word is already in the hashtable
					if (myTable.containsKey(word)) {
						int num = myTable.get(word);
						if (num>=highest) highest=num + 1;
						myTable.put(word, new Integer(++num));
					}
					else {
						myTable.put(word, new Integer(1));
						if (highest==0) highest=1;
					}
				}
				
			}
			
			catch (FileNotFoundException e) {
				System.out.println("File " + data[i] + " was not found! Omitting.");
			}
		}
			
		Enumeration<String> myKeys = myTable.keys();
		
		try {
			BufferedWriter myWriter = new BufferedWriter(new FileWriter(data[data.length-1]));
			
			myWriter.write(highest + "\n");
			while (myKeys.hasMoreElements()) {
				String key = myKeys.nextElement();
				int num = myTable.get(key).intValue();
				myWriter.write(key + " " + num + "\n");
			}
			myWriter.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}