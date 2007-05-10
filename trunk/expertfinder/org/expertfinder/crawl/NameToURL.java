package org.expertfinder.crawl;
import com.yahoo.search.*;

import java.io.*;

import java.util.*;

public class NameToURL {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		String name = "";
		while(sc.hasNextLine())
			name+=sc.next();
		String[] addresses = getAddresses(name, 1, null);
		for(int i=0;i<addresses.length;i++)
			System.out.println(addresses[i]);
	}

	public static String[] getAddresses(String name, int numResults, String withinURL){
		String query = "";
		if(withinURL!=null&&withinURL.length()>0)
			query+="inurl:" + withinURL + " ";

		query+=name;

		ArrayList<String> arrayList = new ArrayList<String>();

        SearchClient client = new SearchClient("OzomwBrV34GSUD6G3vWHVV43nlVF.srrxIT88JqkrDhrTPAqLMhNBwZa6TT.8WuJmCU-");

        // Create the web search request. In this case we're searching for
        // java-related hits.
        WebSearchRequest request = new WebSearchRequest(query);
        request.setResults(numResults);

        try {
            // Execute the search.
            WebSearchResults results = client.webSearch(request);

            // Iterate over the results.
            for (int i = 0; i < results.listResults().length; i++) {
                WebSearchResult result = results.listResults()[i];

                // Print out the document title and URL.
             arrayList.add(result.getUrl());
            }
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
        String[] dummy = new String[0];
        return arrayList.toArray(dummy);
	}

}
