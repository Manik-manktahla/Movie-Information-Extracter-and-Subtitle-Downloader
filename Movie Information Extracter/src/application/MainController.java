package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import Opensubs.OpenSubtitle;
import Opensubs.SubtitleInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TextField movieName;
    @FXML
    private TextField titlefomovie;
    @FXML
    private TextField imdbrating;
    @FXML
    private TextField boxofficecollection;
    @FXML
    private TextField imdbvotes;
    @FXML
    private TextField language;
    @FXML
    private TextField director;
    @FXML
    private Button search;

    @FXML
    private TextField posterlink;
    
    @FXML
    private TextArea plot;
    
    @FXML
    private TextArea actors;
    
    @FXML
    private TextField path;
    @FXML
    private Button download;
    

    String nameofthemovie;
    OpenSubtitle openSubtitle;
    List<SubtitleInfo> info;
    public static final String SEARCH_URL="http://www.omdbapi.com/?s=TITLE&apikey=APIKEY";
	public static final String SEARCH_BY_IMDB_URL="http://www.omdbapi.com/?i=IMDB&apikey=APIKEY";
	
    public void getName(ActionEvent event) throws Exception {
    	nameofthemovie=movieName.getText();
    	String jsonResponse=searchMovieByTitle(nameofthemovie, "852159f0");
		Gson gson=new Gson();
		Movie m=gson.fromJson(jsonResponse, Movie.class);
		List<Search> list=m.getSearch();
		int min=Integer.MAX_VALUE;
		int maxMatch=-1;
		String imdb=new String();
		if(list.size()==0) {
			System.out.println("Not a Movie");
		}
		for(int i=0;i<list.size();i++) 
		   {
		      int index = list.get(i).getTitle().toLowerCase().indexOf(nameofthemovie);
		      if(index > -1 && i < min) 
		      {
		         min = i;
		         maxMatch = i;
		      }
		   }
		   if(maxMatch!=-1)
		   {
		      imdb=list.get(maxMatch).getImdbID();
		   }
		   String jsonResponse2=searchMovieByImdb(imdb,"852159f0");
		   Gson gson2=new Gson();
		   Imdb i=gson2.fromJson(jsonResponse2, Imdb.class);
		   
		   imdbrating.setText(i.getImdbRating());
		   
		    openSubtitle=new OpenSubtitle();
	        openSubtitle.login();
	        
	         info=openSubtitle.getMovieSubsByName(nameofthemovie,"20","eng");

	        titlefomovie.setText(i.getTitle());
	        boxofficecollection.setText(i.getBoxOffice());
	        imdbvotes.setText(i.getImdbVotes());
	        language.setText(i.getLanguage());
	        director.setText(i.getDirector());
	        System.out.println(i.getPoster());
	        posterlink.setText(i.getPoster());
	        plot.setText(i.getPlot());
	        actors.setText(i.getActors());
	        
	        openSubtitle.logOut();
    	
    }
    
    public void getSubtitlesDownloaded(ActionEvent event) {
    	String pathlink=path.getText();
    	String filename=nameofthemovie+".srt";
    	pathlink=pathlink+"\\"+filename;
    	//download.setText("Downloaded");
    	 try {
    		 
			openSubtitle.downloadSubtitle(new URL(info.get(0).getSubDownloadLink().toString().replaceAll(".gz","")),pathlink);
			
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			download.setText("Download Subtitiles");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static String searchMovieByImdb(String imdb,String key) {
		String requestURL=SEARCH_BY_IMDB_URL.replaceAll("IMDB",imdb).replaceAll("APIKEY", key);
		
		return sendGetRequest(requestURL);
	}
    public static String searchMovieByTitle(String title,String key) {
		try {
			title=URLEncoder.encode(title, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String requestURL=SEARCH_URL.replaceAll("TITLE",title).replaceAll("APIKEY", key);
		
		return sendGetRequest(requestURL);
	}
    public static String sendGetRequest(String requestURL) {
		StringBuffer response=new StringBuffer();
		
		try {
			URL url=new URL(requestURL);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Content-Type", "application/json: charset=UTF-8");
			InputStream stream=connection.getInputStream();
			InputStreamReader reader=new InputStreamReader(stream);
			BufferedReader buffer=new BufferedReader(reader);
			String line;
			while((line=buffer.readLine())!=null) {
				response.append(line);
			}
			buffer.close();
			connection.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.toString();
		
	}
}
