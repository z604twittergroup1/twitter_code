package mineHashTags;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
//Author: Aamin Lakhani
public class DriverProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException
	{
		
		//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		// TODO Auto-generated method stub
		// Name of the directory that holds the index 
	    //String directory = "C:\\workspace\\project\\training\\LuceneDemo\\index"; 
		
		String directory ="index1";
	    // Instantiate a new Lucene tool 
	    Indices lucene = new Indices(); 
	    lucene.deserializeMap();
	    // Open the directory 
	    lucene.openIndex(directory, true); 
	    //lucene.readFile();
	
	   
	 
	    String tweet="its an awesome day";
	    String[] tokenizedTweet=tweet.split(" ");
	    for(int i=0;i<tokenizedTweet.length;i++)
	    {
	    	lucene.search(tokenizedTweet[i]);
	    }
	    
	    LinkedList<WordScore> rslt1=lucene.sortResults();
	    LinkedList<WordScore> rslt2=lucene.secondoryFilter(rslt1);
	    lucene.printResults(rslt2);
	    
	    // Close the index 
	    lucene.closeIndex(); 
	}

}
