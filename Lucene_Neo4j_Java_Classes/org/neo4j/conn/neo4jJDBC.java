package org.neo4j.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mineHashTags.LuceneHashTags;
//author Mohamed Mohamed
public class neo4jJDBC {
	public String getRecommendations (String tweet, String uid, String useFollow) throws SQLException, ClassNotFoundException
	{
		LuceneHashTags lh=new LuceneHashTags();
		ArrayList<String> lhash=lh.getLuceneTags(tweet,uid);
		String[] luceneHashtags=new String[lhash.size()];

		luceneHashtags=lhash.toArray(luceneHashtags);
		String neo4jQuery="";
		String neo4jHashtags[]=new String[lhash.size()];
		int neo4jWeights[]=new int[lhash.size()];
		int count=0;
		// Here we do the lucene query

		// Here we store retrieved hashtags in hashtags[]

		// Make sure Neo4j Driver is registered
		//Class.forName("org.neo4j.jdbc.Driver");

		// Connect
		//Connection con = DriverManager.getConnection("jdbc:neo4j://localhost:7474/");

		// Querying
		//uid="18394413";
		//for(int i=0;i<luceneHashtags.length;i++)
		//{
//		if(i>0) neo4jQuery=neo4jQuery+" UNION ";
//		neo4jQuery=neo4jQuery+"MATCH (h:HASHTAG{value:\"" + "chicago" + "\"})-[r:BELONGSTO]->"
//				+ "(u:USER)<-[r2:FOLLOWS]-(u2:USER{id:\""+ uid +"\"}) RETURN h.value AS val, COUNT(h) as w";
		//}

		String recommendations="";
		for(int i=0;i<lhash.size();i++)
		{
			recommendations+="#"+lhash.get(i)+ " ";
		}
		return recommendations;


		//		try(Statement stmt = con.createStatement())
		//		{
		//			ResultSet rs = stmt.executeQuery(neo4jQuery);
		//			while(rs.next())
		//			{
		//				neo4jHashtags[count]=rs.getString("val");
		//				neo4jWeights[count]= Integer.parseInt(rs.getString("w"));
		//				count++;
		//			}
		//
		//
		//
		//			//		    //You have to define formula for adding lucene's results to neo4j's and then send them as recommendations
		//			//		    //note that not all hashtags will return results from neo4j, only one's that match, which might be 0
		//			return recommendations;
		//			//		    
		//			//		    //recommendations should be returned in this form
		//			//		    //"<div style="margin-right:10px;" class="item">#HASHTAG1</div><div style="margin-right:10px;" class="item">#HASHTAG2</div>"
		//		}
	}

}
