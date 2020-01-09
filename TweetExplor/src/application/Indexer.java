package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;



public class Indexer    {
	
	public  String clef;
	public  List<Tweets> arbre;
	public String path;
	
	Directory dir;
	StandardAnalyzer analyzer;
    IndexWriterConfig config ;
    IndexWriter writer;
    List<Tweets> liste_res;
     
  //==================  Constructor For Search  Function =============================
  	public Indexer(List<Tweets> arbre,String path,Directory dir,StandardAnalyzer analyzer, IndexWriterConfig config, IndexWriter writer ) {
  		
  		this.arbre=arbre;
  		this.path=path;
  		this.dir=dir;
  		this.analyzer=analyzer;
  		this.config=config;
  		this.writer=writer;
  		 
  	}
  	
 
	
	//======================================== Load data And Creating Index   Function  =============================
	
public   void imporeter() throws FileNotFoundException{
		
		
			
			File f =new File(path);
		    int nbrAttributTweet = 0;
		    
	        	
		        FileInputStream   inputStream = new FileInputStream(f);
		        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
		        	
		        	String line =br.readLine();
		        	
		        	while((line = br.readLine())!=null){
		        		
		        		//System.out.println(line);
		        		 StringTokenizer tweet = new StringTokenizer (line, "\t");
			                nbrAttributTweet = tweet.countTokens();
			                
				        	 if (nbrAttributTweet != 5) { 
				        	        continue;
				        	      }
				        	
				        	 
				        	 String id_tweet= tweet.nextToken();
				        	 String user=tweet.nextToken();
				        	 String tweet_date=tweet.nextToken();
				        	 String content=tweet.nextToken();  
				        	 String retweet=tweet.nextToken();
				        	 
				        	
					            arbre.add(new Tweets(id_tweet,user,tweet_date,content,retweet));
					            Document doc = new Document();
					        	 
					            doc.add(new TextField("id_tweet",id_tweet, Field.Store.YES));
					            doc.add(new TextField("user",user, Field.Store.YES));
					            doc.add(new TextField("Date", tweet_date, Field.Store.YES));
						        doc.add(new TextField("ligne",content, Field.Store.YES));
						        doc.add(new TextField("retweet", retweet, Field.Store.YES));
						        
						       // System.out.println("Indexing : " + doc);
						        writer.addDocument(doc);
						        
		                          line = br.readLine();

		        }
	        	writer.close();
		        
		        }catch (Exception e ){
					e.printStackTrace();
					
				}
				

	    
		 


		}
	}
