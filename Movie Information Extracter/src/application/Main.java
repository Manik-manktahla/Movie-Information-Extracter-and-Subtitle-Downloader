package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root=FXMLLoader.load(getClass().getResource("Main.fxml"));
		Scene scene=new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Movie Info Extracter");
		//Image image=new Image("\"C:\\Users\\Manik\\eclipse-workspace\\Hii\\src\\icons\\download.jpg\"");
		//primaryStage.getIcons().add(image);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
