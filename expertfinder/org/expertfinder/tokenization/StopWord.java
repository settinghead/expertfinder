package org.expertfinder.tokenization;
import java.io.*;
import java.util.*;

public class StopWord {
	static Hashtable<String, Object> dic = new Hashtable<String, Object>();
	static {

		try {
			FileInputStream input = new FileInputStream("stopwords.txt");
			Object obj = new Object();
			Scanner sc = new Scanner(input);
			while(sc.hasNextLine())
			{
				String word = sc.nextLine();
				if(word.length()>0)
					dic.put(word.trim().toLowerCase(), obj);
			}
			sc.close();
		} catch (FileNotFoundException ex) {
			System.err.println("Warning: cannot read stopword dictionary.");
		}
	}
	
	
	public static boolean isStopword(String word)
	{
		if(dic.get(word)!=null) return true;
		else return false;
	}
}
