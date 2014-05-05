package mineHashTags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

//Author: Aamin Lakhani
public class GetFollowers {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		GetFollowers gf=new GetFollowers();
		gf.readFile();
		
	}
	
	public void serialize(ArrayList<String> list, String id)
	{
		try
		{
			String fileName=id+".ser";
			//System.out.println(hashCounts.keySet().size());
			File f1=new File(fileName);
			FileOutputStream f = new FileOutputStream(f1);
			ObjectOutputStream o = new ObjectOutputStream(f);
			o.writeObject(list);
			o.close();
			f.close();
			System.out.printf("Serialized data ");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void readFile()
	{
		
		String csvFile = "C:/Users/Administrator/workspace2/top_ten.csv";
		String split = ",";
		BufferedReader br = null;
		String line = "";
		String currentId="";
		ArrayList<String> followers=new ArrayList<>();


		try 
		{

			//start of reading file
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) 
			{
				// use comma as separator
				if(!(line.equals("")))
				{
					String[] data = line.split(split);
					if(currentId.equals(""))
					{
						currentId=data[0];
						followers.add(data[1]);
					}
					else if(data[0].equals(currentId))
					{
						followers.add(data[1]);
					}
					else
					{
						
						serialize(followers,currentId);
						currentId=data[0];
						followers.clear();
						followers.add(data[1]);
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
