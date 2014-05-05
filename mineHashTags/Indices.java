package mineHashTags;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


//Author: Aamin Lakhani
public class Indices {
	// The index object, points to a repository
	Directory index;

	// Specify the analyzer for tokenizing text.
	// The same analyzer should be used for indexing and searching
	StandardAnalyzer analyzer;

	IndexWriter writer;

	IndexWriterConfig config;
	
	HashMap<String, WordScore> map=new HashMap<>();
	
	HashMap<String, WordScore> commonTags=new HashMap<>();
	
	HashMap<String, Integer> hashCount=new HashMap<>();
	
	HashMap<String, Integer> popularTags = null;
	
	ArrayList<String> followers=new ArrayList<>();
	
	int hashTagCount=0;
	/**
	 * @param args
	 */
	Indices() {
		// Instantiate the analyzer
		analyzer = new StandardAnalyzer(Version.LUCENE_44);
		config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
	}
	
	public void deserializeFollowers(String uid)
	{
		try
		{
			String file=uid+".ser";
			String file2="C:/Users/Administrator/workspace2/webTest2/"+file;
			FileInputStream fis = new FileInputStream(file2);
			ObjectInputStream ois = new ObjectInputStream(fis);
			followers = (ArrayList) ois.readObject();
			ois.close();
			fis.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		System.out.println("Deserialized Followers..");
	}
	
	public void deserializeMap()//the popular hashtags have already been serialized
	{
		try
		{
			
			FileInputStream fis = new FileInputStream("C:/Users/Administrator/workspace2/webTest2/hashCounts.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			popularTags = (HashMap) ois.readObject();
			System.out.println(popularTags.keySet().size());
			ois.close();
			fis.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		System.out.println("Deserialized HashMap..");
	}

	/* Open or create the index */
	public void openIndex(String directory, boolean newIndex) {
		try {
			// Link the directory on the FileSystem to the application
			index = FSDirectory.open(new File(directory));

			// Check whether the index has already been locked
			// (or not properly closed).
			if (IndexWriter.isLocked(index))
				IndexWriter.unlock(index);
			if (writer == null)
			{
				try
				{
					writer = new IndexWriter(index, config);
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
		} catch (Exception e) {
			System.out.println("Got an Exception: " + e);
		}
	}
	
	public void countTags(String hashTag)
	{
		hashTagCount++;
		if(hashTag.contains("#"))
		{
			String[] tags = hashTag.split("#");
			for(int i=0;i<tags.length;i++)
			{
				if(hashCount.containsKey(tags[i]))
				{
					int c=hashCount.get(tags[i]);
					c++;
					hashCount.put(tags[i], c);
				}
				else
				{
					hashCount.put(tags[i], 1);
				}
			}
		}
		else
		{
			if(hashCount.containsKey(hashTag))
			{
				int c=hashCount.get(hashTag);
				c++;
				hashCount.put(hashTag, c);
			}
			else
			{
				hashCount.put(hashTag, 1);
			}
		}
		
	}
	
	public  void slplt(String hashTag,String tweetText,String user_id,IndexWriter w)
	{
		if(hashTag.contains("#"))
		{
			String[] tags = hashTag.split("#");
			for(int i=0;i<tags.length;i++)
			{
				System.out.println(tags[i]);
				System.out.println("tweet text is"+tweetText);
				try
				{
					addDoc(tweetText,tags[i],user_id);
				}
				catch (Exception e)
				{
				}
			}
		}
		else
		{
			try
			{
				addDoc(tweetText,hashTag,user_id);
			}
			catch (Exception e)
			{

			}
		}


	}

	public  void readFile()
	{

		String csvFile = "/Users/Administrator/workspace2/webTest2/src//prune_tweet.txt";
		String split = "\t";
		BufferedReader br = null;
		String line = "";


		try {

			//start of reading file
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				if(!(line.equals("")))
				{
					String[] tweet = line.split(split);
					int hashInd=tweet.length;
					String tweetText;
					String user_id;
					if(hashInd>0 && (hashInd-2)>0)
					{
						String hashTag= tweet[hashInd-1];
						tweetText=tweet[hashInd-2];
						user_id=tweet[0];
						slplt(hashTag,tweetText,user_id,writer);//uncomment this line for creating index
						//countTags(hashTag);

					}


					//System.out.println("Hahtag is"+hashTag);
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

		System.out.println("Done");
		System.out.println(hashTagCount);
	}

	public void search(String searchTerm) 
	{
		//	Text to search
		String querystr =  searchTerm;
		try
		{
			Query q = new QueryParser(Version.LUCENE_44, "tweet", analyzer).parse(querystr);

			// Searching code
			int hitsPerPage = 50;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			//	Code to display the results of search
			System.out.println("Found " + hits.length + " hits.");
			for(int i=0;i<hits.length;++i) 
			{
				int docId = hits[i].doc;
				float score=hits[i].score;
				//System.out.println(" score "+score);
				Document d = searcher.doc(docId);
				removeDuplicates(d.get("hashTag"),d,score,querystr);
//				if(hashTagCount==50)
//				{
//					break;
//				}
				//System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
			}

			reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		//	The \"title\" arg specifies the default field to use when no field is explicitly specified in the query


		// reader can only be closed when there is no need to access the documents any more

	}
	
	public void removeDuplicates(String hashTag,Document id,float score, String searchTerm)
	{
		if(!(map.containsKey(hashTag)))
		{
			WordScore w=new WordScore();
			w.setWord(searchTerm);
			w.setScore(score);
			String flwr=id.get("user_id");
			if(followers.contains(flwr))
			{
				w.setFpriority(w.getFpriority()+1);
			}
			map.put(hashTag, w);
			if(popularTags.containsKey(hashTag))
			{
				w.setHashTagCount(popularTags.get(hashTag));
			}
			hashTagCount++;
		}
//		else
//		{
//			WordScore wd= map.get(hashTag);
//			if(!(wd.getWord()==searchTerm))
//			{
//				if(!(commonTags.containsKey(hashTag)))
//				{
//					commonTags.put(hashTag, map.get(hashTag));
//				}
//			}
//			//commonTags.put(hashTag, id);
//		}
	}
	
	public LinkedList<WordScore> tertiaryFilter(LinkedList<WordScore> rslt)
	{
		LinkedList<WordScore> result=new LinkedList<>();
		//int loopLimit=((rslt.size()>10)?10:rslt.size());
		
		for(int i=0;i<rslt.size();i++)
		{
			WordScore w=rslt.get(i);
			int pos=getTerInsertionPoint(result, w.getFpriority());
			result.add(pos, w);
		}
		
		return result;
	}
	
	public int getTerInsertionPoint(LinkedList<WordScore> list,float count)
	{
		int index=-1;
		if(list.size()==0)
		{
			index=0;
		}
		else
		{
			for(int i=0;i<list.size();i++)
			{
				WordScore p=list.get(i);
				if(p.getFpriority()<count)
				{
					index=i;
					break;
				}
				index=i+1;;
			}
			
		}
		
		return index;	
	}
	
	public void printResults()
	{
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()) {
		    String key = (String)iter.next();
		    WordScore idd = map.get(key);
		    System.out.println("Hashtag " + key + "Score=" + idd.getScore());
		}
		System.out.println("Total hashtags"+hashTagCount);
	}
	
	public void printResults(LinkedList<WordScore> rslt)
	{

		
		for(int i=0;i<rslt.size();i++)
		{
			WordScore w=rslt.get(i);
			System.out.println(w.getHashTag()+ " "+w.getHashTagCount());
//			if(i==10)
//			{
//				break;
//			}
		}
	}
	
	public LinkedList<WordScore> secondoryFilter(LinkedList<WordScore> rslt)
	{
		LinkedList<WordScore> result=new LinkedList<>();
		int loopLimit=((rslt.size()>25)?25:rslt.size());
		
		for(int i=0;i<loopLimit;i++)
		{
			WordScore w=rslt.get(i);
			int pos=getSecInsertionPoint(result, w.getHashTagCount());
			result.add(pos, w);
		}
		
		return result;
	}
	
	public int getSecInsertionPoint(LinkedList<WordScore> list,float count)
	{
		int index=-1;
		if(list.size()==0)
		{
			index=0;
		}
		else
		{
			for(int i=0;i<list.size();i++)
			{
				WordScore p=list.get(i);
				if(p.getHashTagCount()<=count)
				{
					index=i;
					break;
				}
				index=i+1;;
			}
			
		}
		
		return index;	
	}
	
	public LinkedList<WordScore> sortResults()
	{
		LinkedList<WordScore> rslt=new LinkedList<>();
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()) 
		{
		    String key = (String)iter.next();
		    WordScore idd = map.get(key);
		    int pos=getInsertionPoint(rslt, idd.getScore());
		    idd.setHashTag(key);
		    rslt.add(pos, idd);
		}
		
		return rslt;
	}
	
	public int getInsertionPoint(LinkedList<WordScore> list,float score)
	{
		int index=-1;
		if(list.size()==0)
		{
			index=0;
		}
		else
		{
			for(int i=0;i<list.size();i++)
			{
				WordScore p=list.get(i);
				if(p.getScore()<=score)
				{
					index=i;
					break;
				}
				index=i+1;;
			}
			
		}
		
		return index;	
	}

	public  void addDoc(String tweet, String hashTag,String user_id) throws IOException 
	{
		Document doc = new Document();
		// A text field will be tokenized
		doc.add(new TextField("tweet", tweet, Field.Store.YES));
		// We use a string field for isbn because we don\'t want it tokenized
		doc.add(new StringField("hashTag", hashTag, Field.Store.YES));
		doc.add(new StringField("user_id", user_id, Field.Store.YES));
		writer.addDocument(doc);
	}

	// Close the index
	public void closeIndex() {
		try {
			writer.commit();
			writer.close();
		} catch (Exception e) {
			System.out.println("Got an Exception: " + e);
		}
	}
}
