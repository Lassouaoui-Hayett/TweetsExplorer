package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class Tweets implements Comparator<Tweets>   {
	
	private   String  id_tweet;
	private   String  user;
	private   String date;
	private   String  tweet;
	private   String  retweet;
	Date date1;
	Date date2;
	
public Tweets(String  id_tweet,String  user,String date, String tweet, String  retweet) {
		
	
	    this.id_tweet=  id_tweet;
	    this.user = user;
		this.date = date;
		this.tweet = tweet;
		this.retweet = retweet;
		
}
public Tweets() {
	
}


public String getId_tweet() {
	return id_tweet;
}
public void setId_tweet(String id_tweet) {
	this.id_tweet = id_tweet;
}
public String getUser() {
	return user;
}
public void setUser(String user) {
	this.user = user;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getTweet() {
	return tweet;
}
public void setTweet(String tweet) {
	this.tweet = tweet;
}
public String getRetweet() {
	return retweet;
}
public void setRetweet(String retweet) {
	this.retweet = retweet;
}


//==================  Display  List Function =============================

public String toString(){
	
	return("la date du tweet ==> " + date
			+"\t "+ "le tweet ==> "  + tweet
			+"\t "+ "le id_tweet ==> "  + id_tweet
			+"\t "+ "le user ==> "  + user
			+"\t "+ "le retweet ==> "  + retweet
			
			 );
}


//==================  Sort By Date   Function =============================

public void TrieParDate(List<Tweets> arbre){
	
	 Collections.sort(arbre, new Comparator<Tweets>() {
		
		  public int compare(Tweets o1, Tweets o2) {
			  try {
				   //convertire la date qui est en string en format Date
				  //2019-06-21 11:46:36.162995
			 date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(o1.getDate());
			 date2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(o2.getDate());
			
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
		      if (date1 == null || date2 == null)
		        return 0;
		      return o1.getDate().compareTo(o2.getDate());
		  }
		});
}

@Override
public int compare(Tweets o1, Tweets o2) {
	// TODO Auto-generated method stub
	return 0;
}

}

