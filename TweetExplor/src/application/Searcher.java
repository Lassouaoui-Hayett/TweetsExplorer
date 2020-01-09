package application;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

public class Searcher {

	public String clef;
	public List<Tweets> arbre;
	public String path;
	
	Directory dir;
	StandardAnalyzer analyzer;
    IndexWriterConfig config ;
    IndexWriter writer;
    List<Tweets> liste_res;
    
    //==================  Constructor For Load And Index File  Function =============================
   	public Searcher(String clef,List<Tweets> arbre,Directory dir,StandardAnalyzer analyzer, IndexWriterConfig config, IndexWriter writer){
   		this.clef=clef;
   		this.arbre=arbre;
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
//  		            System.out.println("score: " + result.score + 
//  		            		" -- text: " + resultDoc.get("ligne")+"   "+"date du tweet ==> "+"   "+resultDoc.get("Date"));
  		            
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
  	
}
