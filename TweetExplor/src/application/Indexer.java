package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

import java.util.ArrayList;


public class Indexer    {
	
	public String clef;
	public List<Tweets> arbre;
	public String path;
	
	Directory dir;
	StandardAnalyzer analyzer;
    IndexWriterConfig config ;
    IndexWriter writer;
    List<Tweets> liste_res;
     
  //==================  Constructor For Load And Index File  Function =============================
	public Indexer(String clef,List<Tweets> arbre,Directory dir,StandardAnalyzer analyzer, IndexWriterConfig config, IndexWriter writer){
		this.clef=clef;
		this.arbre=arbre;
		this.dir=dir;
		this.analyzer=analyzer;
		this.config=config;
		this.writer=writer;
		
	}
	
	//==================  Constructor For Search  Function =============================
	public Indexer(List<Tweets> arbre,String path,Directory dir,StandardAnalyzer analyzer, IndexWriterConfig config, IndexWriter writer ) {
		
		this.arbre=arbre;
		this.path=path;
		this.dir=dir;
		this.analyzer=analyzer;
		this.config=config;
		this.writer=writer;
		 
	}
	
//======================================== Search Function =============================
	public List<Tweets> recherche(){
		
			try { 
				
		        
				 liste_res =new ArrayList<Tweets>();
	
				//ouvrir un IndexSearcher
		        IndexReader reader = DirectoryReader.open(dir);
		        IndexSearcher searcher = new IndexSearcher(reader);
		       
				//creation d'une requette pour la recherche du mot clef dans la ligne du Tweet
		        QueryParser parser = new QueryParser("ligne", analyzer);
		        
		        //ajout du mot clef a rechercher dans la requette
		        Query query = parser.parse(clef);
		    
		        //recherche du resultat de la requette dans l'indexe
		        System.out.println("Searching for: \"" + query + "\"");
		        
		        //preciser le max de resultat qu'on veux avoir dans notre requette
		        TopDocs results = searcher.search(query, 30);
		        
		        //explorer les resultat obtenue
		        for (ScoreDoc result : results.scoreDocs) {
		        	
		            Document resultDoc = searcher.doc(result.doc);
//		            
//		            System.out.println("score: " + result.score + 
//		            		" -- text: " + resultDoc.get("ligne")+"   "+"date du tweet ==> "+"   "+resultDoc.get("Date"));
		            
		            //remplire une array list par le resultat obtenu
		            Tweets t_resultat =new Tweets(resultDoc.get("id_tweet"),resultDoc.get("user"),
		            		resultDoc.get("Date"),resultDoc.get("ligne"),resultDoc.get("retweet"));
		            liste_res.add(t_resultat);
		            
		        }
		        //System.out.println("l'arbre est :\n"+arbre);
		        reader.close();
		        
		      
			   }catch (Exception e ){
				e.printStackTrace();
				
			}
	           return   liste_res;		
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
