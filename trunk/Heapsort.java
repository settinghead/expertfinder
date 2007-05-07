package org.expertfinder.profilequery;
import java.util.*;

public class Heapsort 
{
    private static Sim[] data;
    private static int n;
    

    /** 
	 * This method sorts the data examples and the sorting is done on the parameter attrParam.
	 * 
	 * @param examples The list of data examples
	 * @param nParam The number of examples
	 * @param attrParam The attribute on which sorting is to be done
	 *        if == -1 sort examples based on the weights
	 **/
    public static void sort(Sim[] examples, int numOfExamples)
    {
        data = examples;        
        n = numOfExamples;
        
        // first build a heap, then keep exchanging the 1st and last elements and call downheap
        buildheap();
        while (n > 1)
        {
            n--;
            exchange (0, n);
            downheap (0);
        } 
    }

    // buildheap will result in an array which satisfies the heap properties
    private static void buildheap()
    {
        for (int i = n/2-1; i >= 0; i--)
            downheap (i);
    }

    // percolate down the root of the tree until the heap propertes are satisfied
    private static void downheap(int curr)
    {
        int bestChild = 2 * curr + 1;    // first descendant of v
        StringTokenizer tokenizer = null;
        String token = "";
        
        // keep percolating down
        while (bestChild < n)
        {        	
        	double currVal = data[curr].auth;        	        	
        	double childVal = data[bestChild].auth;
        	
            if (bestChild + 1 < n)    // is there a second descendant?
            {
            	double sibVal = data[bestChild+1].auth;
            	
            	if (sibVal < childVal) {
            		childVal = sibVal;
            		bestChild++;
            	}            	
            }           

            if (currVal <= childVal) return;  // v has heap property
            // otherwise
            exchange(curr, bestChild);  // exchange labels of v and w
            curr = bestChild;        // continue
            bestChild = 2*curr + 1;
        }
    }


    private static void exchange(int i, int j)
    {
        Sim temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    
}    // end class HeapSorter
