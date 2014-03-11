import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by smoker on 3/11/14.
 */
public class hash_tag_cleaner {

        public static void main(String args[]){
            String path1="C:\\aggy";// source directory
            File f1= new File(path1+"\\agg.csv");//directory file
            FileWriter f0;//writes to output file
            int line=0;

            try{
                Scanner scan=new Scanner(f1).useDelimiter("\n");//scans each line
                f0 = new FileWriter(path1+"\\aggy.csv",true);// creation of new file writer


                while(scan.hasNext())//scanning of each file
                {
                    Scanner scan2=new Scanner(scan.next()).useDelimiter(",");
                    String string1[]= new String[3];

                    for(int i=0; i<string1.length;i++)
                        string1[i]=scan2.next();
                    scan2.close();

                    scan2=new Scanner(string1[1]).useDelimiter(" ");

                    String string2[]=new String[6];
                    for(int i=0; i<string2.length; i++)//pulls each record file
                        string2[i]=scan2.next();

                    scan2.close();

                    scan2=new Scanner(string1[2]).useDelimiter(" ");

                    String test=scan2.next();

                    List<String> a1 = new ArrayList<String>();
                    int var1=0;


                    if(test.length()>=2)
                    {
                        a1.add(test);
                        var1++;
                    }

                    while(scan2.hasNext())
                    {
                        a1.add(scan2.next());
                        var1++;
                    }
                    scan2.close();


                    for (int i=0; i<var1; i++)
                        //writes token to file
                        f0.write(string1[0]+","+string2[0]+","+string2[2]+"-"+string2[1]+"-"+string2[5]+"," +
                                string2[3]+" "+ string2[4]+","+a1.get(i)+"\n");
                    //flushes buffer data
                    f0.flush();
                    System.out.print(line+"\n");
                    line++;


                }

                
                scan.close();
                f0.close();

            }
            catch
                    (IOException e) {
                System.out.println("I/O Error: " + e);

            }
        }
    }


