import java.util.regex.*;

public class HTML2Text {
	static String htmlToText(String html)
	{
		return Pattern.compile("<[^>]*>").matcher(html).replaceAll(" ");
	}
	
	public static void main(String args[])
	{
		System.out.println(htmlToText("<html><head><title>java</title></head><body>rocks?</body></html>"));
	}
}
