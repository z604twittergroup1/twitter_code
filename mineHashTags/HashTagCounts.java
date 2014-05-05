package mineHashTags;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
//Author: Aamin Lakhani
public class HashTagCounts 
{

	public HashMap<String, Integer> hashCounts;
	
	public HashTagCounts()
	{
		hashCounts=new HashMap<>();
	}

	public static void main(String args[]) throws IOException
	{

		HashTagCounts ht=new HashTagCounts();
		ht.createMap();
		//ht.serializeMap();
		ht.deserializeMap();
		//ht.printMap();
	}

	public void printMap(HashMap<String, Integer> map) 
	{
		int cnt=0;
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()) 
		{
		    String key = (String)iter.next();
		    Integer val = map.get(key);
		    System.out.println("Hashtag " + key + "Score=" + val);
		    //cnt+=val;
		}
		//System.out.println(cnt);
		
	}

	public void deserializeMap()
	{
		HashMap<String, Integer> map = null;
		try
		{
			FileInputStream fis = new FileInputStream("hashCounts.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			map = (HashMap) ois.readObject();
			System.out.println("lol"+map.keySet().size());
			ois.close();
			fis.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		System.out.println("Deserialized HashMap..");
		printMap(map);


	}

	public void serializeMap()
	{
		try
		{
			//System.out.println(hashCounts.keySet().size());
			FileOutputStream f = new FileOutputStream("hashCounts.ser");
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(hashCounts);
			o.close();
			f.close();
			System.out.printf("Serialized HashMap data is saved in hashCounts.ser");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addToMap(String hashTags)
	{
		if(hashTags.contains("#"))
		{
			String[] tags = hashTags.split("#");
			for(int i=0;i<tags.length;i++)
			{
				if(hashCounts.containsKey(tags[i]))
				{
					int c=hashCounts.get(tags[i]);
					c++;
					hashCounts.put(tags[i], c);
				}
				else
				{
					hashCounts.put(tags[i], 1);
				}
			}
		}
		else
		{
			if(hashCounts.containsKey(hashTags))
			{
				int c=hashCounts.get(hashTags);
				c++;
				hashCounts.put(hashTags, c);
			}
			else
			{
				hashCounts.put(hashTags, 1);
			}
		}
	}

	public void createMap()
	{
		
		String csvFile = "/Users/Administrator/workspace2/webTest2/src//prune_tweet.txt";
		String split = "\t";
		BufferedReader br = null;
		String line = "";


		try 
		{

			//start of reading file
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) 
			{
				// use comma as separator
				if(!(line.equals("")))
				{
					String[] tweet = line.split(split);
					int hashInd=tweet.length;
					if(hashInd>0 && (hashInd-2)>0)
					{
						String hashTag= tweet[hashInd-1];
						addToMap(hashTag);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
