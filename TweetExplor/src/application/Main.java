package application;

import javafx.scene.control.Button;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
//import javafx.beans.value.ObservableValue;

//import javafx.beans.value.ChangeListener;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Main extends Application {
	
	 private static  List<Tweets> arbre;
	 
	TableView<Tweets> table = new TableView<Tweets>();
	 ProgressBar progressBar = new ProgressBar();
	 String path;
	 Tweets t = new Tweets();
	 
	 //Creation d'un Indexeur pour le passer en parametre du constructeur
     Directory dir = new RAMDirectory();
     StandardAnalyzer analyzer;
     IndexWriterConfig config ;
     IndexWriter writer;
 	 List<Tweets> ll;
	
    public void start(Stage stage) {
		
		 final FileChooser fileChooser = new FileChooser();
		 configuringFileChooser(fileChooser);
		
		
//========================= Menu bare Creation ============================	
		
        MenuBar menuBar = new MenuBar();
        
        Menu fileMenu = new Menu("File");
        Menu trieMenu = new Menu("Trie");
   
        TextField textField = new TextField ();
        textField.setMinWidth(180);
        
        
        Button btnRight = new Button("recherche");
        
        Button afficher = new Button("Afficher resultat");
        afficher.setDisable(true);
       
        Label label = new Label();
		label.setPadding(new Insets(5, 5, 5, 5));
        
        MenuItem openFileItem = new MenuItem("Open File");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem trie_d = new MenuItem("Trie par date");
       // MenuItem trie_t = new MenuItem("Trie par tweet");
        
        
        //creation des columns de la TABLEView
        TableColumn<Tweets, String> id_tweet //
                = new TableColumn<Tweets, String>("Id Tweet");
   
        TableColumn<Tweets, String> user//
                = new TableColumn<Tweets, String>("User");
        
        TableColumn<Tweets, String> tweet_date//
               = new TableColumn<Tweets, String>("Date Tweet");
        
        TableColumn<Tweets, String> content//
                = new TableColumn<Tweets, String>("Tweet");
        
        TableColumn<Tweets, String> retweet//
                = new TableColumn<Tweets, String>("Retweet");
        
        //rechercher les attribue depuis la class Tweet
        id_tweet.setCellValueFactory(new PropertyValueFactory<>("id_tweet"));
        user.setCellValueFactory(new PropertyValueFactory<>("user"));
        tweet_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        content.setCellValueFactory(new PropertyValueFactory<>("tweet"));
        retweet.setCellValueFactory(new PropertyValueFactory<>("retweet"));
        
        user.setSortType(TableColumn.SortType.DESCENDING);
        
        table.getColumns().addAll(id_tweet, user, tweet_date, content,retweet);

   
        
        trie_d.setDisable(true);
       // trie_t.setDisable(true);
        
//=======================  File Event ==========================  
        
        openFileItem.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	
                    	 File file = fileChooser.showOpenDialog(stage);
                         if (file != null) {
                            
                             try {
                            	 
								 path= file.getCanonicalPath();
								 //passer en parametre les eleent necessaire pour cree un indexeur et un analyseur
				                 Indexer kk =new Indexer(arbre = new ArrayList<Tweets>(),path,dir = new RAMDirectory(),analyzer = new StandardAnalyzer(),
				                		 config = new IndexWriterConfig(analyzer),new IndexWriter(dir, config));
								 kk.importer();
								 ObservableList<Tweets> list = FXCollections.observableArrayList(arbre);						
								 table.setItems(list);						
								 
							} catch (IOException e1) {
								
								e1.printStackTrace();
							}
                         }	
                    }
                });

//=======================  Sort By Date Event ==========================  
        
        trie_d.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                    	
                    	table.getItems().clear();
                    	t.TrieParDate(ll);
                    	 ObservableList<Tweets> list = FXCollections.observableArrayList(ll);						
        				 table.setItems(list);	
                    	
                        
                    }
                });

//=======================  EXIT Event ========================== 
        
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        
//=======================  Search Event ========================== 
        
        btnRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	
              if(textField.getText().length() !=0){
            	  
            	  if(table.getItems() != null){
            		        table.getItems().clear();
            		        
            	                                }
            	  
            	Indexer it =new Indexer(textField.getText(),arbre,dir, analyzer,  config,  writer);
            
                 ll=it.recherche();
            	 //System.out.println("l'arbre est :\n"+ll);
            	 ObservableList<Tweets> list = FXCollections.observableArrayList(ll);						
				 table.setItems(list);	
				 trie_d.setDisable(false);

              }
              
            
            }
        });
        

//======================= Add Menu Items ========================= 
        
        fileMenu.getItems().addAll( openFileItem, exitItem);
        trieMenu.getItems().addAll(trie_d);
        menuBar.getMenus().addAll(fileMenu, trieMenu);
       

//======================== Add elements To Root ===================
        
        BorderPane root = new BorderPane();
        
        root.setTop(menuBar);
        
        root.setCenter(table);
        BorderPane.setMargin(table, new Insets(40, 10, 10, 0));
        
        root.setRight(btnRight);
        BorderPane.setMargin(btnRight, new Insets(5, 5, 5, 5));
               
        root.setLeft(textField);
        BorderPane.setMargin(textField, new Insets(5, 5, 5, 5));
//       
//        root.setBottom(label);
        
        Scene scene = new Scene(root, 600, 600);
     
        stage.setTitle(" Tweets Explorer ");
        stage.setScene(scene);
        stage.show();
    }
	
	 private void configuringFileChooser(FileChooser fileChooser) {
	      // Set title for FileChooser
	      fileChooser.setTitle("Select File :)");
	 
	      // ajouter une extention filtre
	      fileChooser.getExtensionFilters().addAll(
	              new FileChooser.ExtensionFilter("fichier texte", "*.txt*"), 
	              new FileChooser.ExtensionFilter("fichier csv", "*.csv") 
	                                                            );
	  }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
}
