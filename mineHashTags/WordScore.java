package mineHashTags;
//Author: Aamin Lakhani
public class WordScore 
{
	
	private String word;
	
	private float score;
	
	private String hashTag;
	
	private int hashTagCount;
	
	private int fpriority=0;
	
	public void setHashTagCount(int hashTagCount)
	{
		this.hashTagCount=hashTagCount;
	}
	
	public int getHashTagCount()
	{
		return this.hashTagCount;
	}
	
	public void setHashTag(String hashTag)
	{
		this.hashTag=hashTag;
	}
	
	public String getHashTag()
	{
		return this.hashTag;
	}
	
	public void setWord(String word)
	{
		this.word=word;
	}
	
	public void setScore(float score)
	{
		this.score=score;
	}
	
	public String getWord()
	{
		return this.word;
	}
	
	public float getScore()
	{
		return this.score;
	}
	
	public void setFpriority(int fpriority)
	{
		this.fpriority=fpriority;
	}
	
	public int getFpriority()
	{
		return this.fpriority;
	}

}
