package org.expertfinder.profilequery;
import java.io.*;
import java.util.*;

public class WordFreq {
	private static Hashtable count = new Hashtable();
	
	public static void main(String[] args) throws Exception{
		int docNum = 0;
		Scanner s = new Scanner(new File(args[0]));
		while (s.hasNextLine()) {
			docNum++;
			String name = s.nextLine().trim().split(" ")[0];
			Scanner s1 = new Scanner(new File(name));
			while (s1.hasNextLine()) {
				String token = s1.nextLine().trim().split(" ")[0];
				Int obj = (Int)count.get(token);
				if (obj == null) {
					count.put(token, new Int(1));
				}
				else obj.value++;
			}
			s1.close();
		}
		s.close();
		
		FileWriter writer = new FileWriter(new File(args[1]));
		
		writer.write(docNum);
		
		for (Enumeration e = count.keys(); e.hasMoreElements(); ) {
			String token = (String)e.nextElement();
			int value = ((Int)count.get(token)).value;
			writer.write(token + " " + value);
		}
		writer.flush();
		writer.close();
	}
}

