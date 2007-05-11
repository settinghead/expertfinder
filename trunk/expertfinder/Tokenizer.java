

import java.io.*;
import java.util.Scanner;
import java.util.regex.*;

public class Tokenizer {

	public static void tokenize(String input, Writer writer) {
		try {
			// Scanner sc = new Scanner(input);


			Pattern p = Pattern.compile("[a-zA-Z\\-]*[^a-zA-Z\\-]*");
			Pattern nonAlnum = Pattern.compile("[^a-zA-Z\\-]");
			Matcher m = p.matcher(input);
			int count = 0;
			String word = "";
			while (m.find()) {
				try {
					word = input.substring(m.start(), m.end());

					// truncate
					word = nonAlnum.matcher(word).replaceAll("");
					// handle line break
					if (word.length() > 2 && word.length() < 25) {
						if (word.charAt(word.length() - 1) == '-' && m.find())
							word = word.substring(0, word.length() - 1)
									+ nonAlnum
											.matcher(
													input.substring(m.start(),
															m.end()))
											.replaceAll("");
						if (!StopWord.isStopword(word.trim().toLowerCase())) {
							writer.write(word.toLowerCase() + " ");
							count++;
						}

						if (count >= 1000) {
							writer.write("\r\n");
							writer.flush();
							count = 0;
						}
					}
				} catch (Exception ex) {
					// System.err.print(ex.getMessage());
					// System.err.println(" " + word);
					try {
						writer.write("\r\n");
						writer.flush();
					} catch (IOException exx) {
					}
					;
				}
			}
			try {
				writer.write("\r\n");
				writer.flush();
			} catch (IOException ex) {
				System.err.print(ex.getMessage());
			}
			// sc.close();
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
