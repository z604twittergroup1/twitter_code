package mineHashTags;

import java.util.ArrayList;
import java.util.LinkedList;
//Author: Aamin Lakhani
public class LuceneHashTags 
{
	public ArrayList<String> tags =new ArrayList<>();

	public ArrayList<String> getLuceneTags(String tweet,String uid)
	{
		String directory ="C:/Users/Administrator/workspace2/webTest2/index1"; 
	    Indices lucene = new Indices();
	    lucene.deserializeFollowers(uid);
	    lucene.deserializeMap();
	    // Open the directory 
	    lucene.openIndex(directory, true); 
	    
	    
	    String[] tokenizedTweet=tweet.split(" ");
	    for(int i=0;i<tokenizedTweet.length;i++)
	    {
	    	lucene.search(tokenizedTweet[i]);
	    }
	    
	    LinkedList<WordScore> rslt1=lucene.sortResults();
	    LinkedList<WordScore> rslt2=lucene.secondoryFilter(rslt1);
	    LinkedList<WordScore> rslt3=lucene.tertiaryFilter(rslt2);
	    
	    for(int i=0;i<rslt3.size();i++)
		{
			WordScore w=rslt2.get(i);
			tags.add(w.getHashTag());
			//System.out.println(w.getHashTag()+ " "+w.getHashTagCount());

		}
		
		return tags;
	}

}
