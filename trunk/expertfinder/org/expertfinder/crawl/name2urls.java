package org.expertfinder.crawl;
import com.yahoo.search.*;

import java.io.*;


public class name2urls {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getAddresses("jerry zhu", 5, null, System.out);
	}
	
	public static void getAddresses(String name, int numResults, String withinURL, OutputStream out){
		String query = "";
		if(withinURL!=null&&withinURL.length()>0)
			query+="inurl:" + withinURL + " ";
		
		query+=name;
		
        SearchClient client = new SearchClient("OzomwBrV34GSUD6G3vWHVV43nlVF.srrxIT88JqkrDhrTPAqLMhNBwZa6TT.8WuJmCU-");

        // Create the web search request. In this case we're searching for
        // java-related hits.
        WebSearchRequest request = new WebSearchRequest(query);
        request.setResults(numResults);

        try {
            // Execute the search.
            WebSearchResults results = client.webSearch(request);

            OutputStreamWriter sw = new OutputStreamWriter(out);
            // Iterate over the results.
            for (int i = 0; i < results.listResults().length; i++) {
                WebSearchResult result = results.listResults()[i];

                // Print out the document title and URL.
                sw.write(result.getUrl()+"\n");
            }
            sw.flush();
            sw.close();
        }
        catch (IOException e) {
            // Most likely a network exception of some sort.
            System.err.println("Error calling Yahoo! Search Service: " +
                    e.toString());
            e.printStackTrace(System.err);
        }
        catch (SearchException e) {
            // An issue with the XML or with the service.
            System.err.println("Error calling Yahoo! Search Service: " +
                    e.toString());
            e.printStackTrace(System.err);
        }
	}
	
}
