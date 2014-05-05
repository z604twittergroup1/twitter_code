package mineHashTags;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
//Author: Aamin Lakhani
public class CalculatePrecision {

	/**
	 * @param args
	 */
	public static  int positiveCount=0;
	public static int negativeCount=0;

	public static void main(String[] args) 
	{

		CalculatePrecision cp=new CalculatePrecision();

		String directory ="index1";
		// Instantiate a new Lucene tool 
		Indices lucene = new Indices();
		lucene.deserializeFollowers("17105182");
		lucene.deserializeMap();
		// Open the directory 
		lucene.openIndex(directory, true); 

		lucene.closeIndex(); 

		cp.readFile(lucene);
		System.out.println("positive is"+positiveCount);
		System.out.println("negative is"+negativeCount);

	}

	public void predictHashTag(String tweet, String originalTag,Indices lucene)
	{
		String[] tokenizedTweet=tweet.split(" ");
		for(int i=0;i<tokenizedTweet.length;i++)
		{
			lucene.search(tokenizedTweet[i]);
		}

		LinkedList<WordScore> rslt1=lucene.sortResults();
		LinkedList<WordScore> rslt2=lucene.secondoryFilter(rslt1);
		LinkedList<WordScore> rslt3=lucene.tertiaryFilter(rslt2);

		checkHashTag(rslt3, originalTag);
	}

	public void checkHashTag(LinkedList<WordScore> rslt3,String originalTag)
	{
		Boolean flag=false;
		for(int i=0;i<rslt3.size();i++)
		{
			WordScore w=rslt3.get(i);
			if(w.getHashTag().equals(originalTag))
			{
				positiveCount++;
				flag=true;
				break;
			}
		}
		if(flag==false)
		{
			negativeCount++;
		}
	}

	public  void readFile(Indices lucene)
	{

		String csvFile = "/Users/Administrator/workspace2/webTest2/src//prune_tweet.txt";
		String split = "\t";
		BufferedReader br = null;
		String line = "";


		try {

			//start of reading file
			br = new BufferedReader(new FileReader(csvFile));

			for(int i=0;i<500;i++)
			{

				while ((line = br.readLine()) != null) {

					// use comma as separator
					if(!(line.equals("")))
					{
						String[] tweet = line.split(split);
						int hashInd=tweet.length;
						String tweetText;
						if(hashInd>0 && (hashInd-2)>0)
						{
							String hashTag= tweet[hashInd-1];
							tweetText=tweet[hashInd-2];
							predictHashTag(tweetText,hashTag,lucene);
						}
					}


				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
