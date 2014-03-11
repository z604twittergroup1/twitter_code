
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Tweet_Cleaner {

    public static void main(String args[]){
        String path1="E:\\New_Twitter\\Tweet\\Dump5";// source directory
        File f1= new File(path1);//directory file
        File f2;// file to represent each individual file
        Scanner scan;//scans each line
        FileWriter f0;//writes to output file
        String[] dir= f1.list();//stores source directory file listing
        String[] s= new String[12];//holds each tweet line
        int a=0,b=0,line=1;

try{

    f0 = new FileWriter(path1+"\\archive\\Tweets"+a+".txt",true);// creation of new file writer

    for(int j=0; j<dir.length; j++)//will run until the end of the directory is reached
        {
            System.out.println("j"+j);

    f2= new File(path1+"\\"+dir[j]);//creation of new file to read
    scan= new Scanner(f2);
    while(scan.hasNext())//scanning of each file
    {

        for(int i=0; i<12; i++)//pulls each record file
        {
        scan.useDelimiter("\n");
            String token;
            token=scan.next();
			
			
			//checks an incoming string to see if there is additional data. Tweet data does not continue on one line
			//program appends that data onto the previous segment.
             while(!(token.equals("***")||token.regionMatches(false,0,"Type:",0,5)||
                     token.regionMatches(false,0,"Origin:",0,7)||token.regionMatches(false,0,"Text:",0,5)||
                     token.regionMatches(false,0,"URL:",0,4)||token.regionMatches(false,0,"ID:",0,3)||
                     token.regionMatches(false,0,"Time:",0,5)||token.regionMatches(false,0,"RetCount:",0,9)||
                     token.regionMatches(false,0,"Favorite:",0,9)||
                     token.regionMatches(false,0,"MentionedEntities:",0,18)||
                     token.regionMatches(false,0,"Hashtags:",0,9)))
                {
                    s[i-1]=s[i-1]+" "+token;
                    token=scan.next();

                }

        s[i]= token;
		//outputs the processed line to console
        System.out.print(dir[j] +" processed line "+line+"\n");
        line++;

        }
		//writes token to file
        f0.write(f2.getName()+"\t"+s[1].substring(5,s[1].length())+
                "\t"+s[2].substring(8,s[2].length())+"\t"+s[3].substring(6,s[3].length())+
                "\t"+s[4].substring(5,s[4].length())+"\t"+s[5].substring(4,s[5].length())+
                "\t"+s[6].substring(6,s[6].length())+"\t"+s[7].substring(10,s[7].length())+
                "\t"+s[8].substring(10,s[8].length())+"\t"+s[9].substring(19,s[9].length())+
                "\t"+s[10].substring(10,s[10].length())+"\n");
				//flushes buffer data
        f0.flush();


    }

	//prevents output file from becoming too large. Creates new file after 900 users
    scan.close();
    line=1;
    f2.renameTo(new File(path1+"\\processed\\"+dir[j]));
     b++;
     if(b>900)
     {
         b=0;
         a++;
         f0 = new FileWriter(path1+"\\archive\\Tweets"+a+".txt");
     }

    }
    f0.close();

       }
catch
(IOException e) {
    System.out.println("I/O Error: " + e);

}
    }
}
