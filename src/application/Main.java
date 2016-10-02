
package application;
	
import java.io.IOException;
import java.util.ArrayList;
import application.view.chatAppController;
import application.view.loginController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;



public class Main extends Application {
	public static final String CODE_NAME ="&@^*&FGDAJFG"; 
	public static final String Logout_Name_code ="@#$*&jgjgj!1";
	public static void main(String[] args) {
		launch(args);
	}	
	@Override
	public void start(Stage primaryStage) {
		try { 
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/login.fxml"));
			Parent root = loader.load();
			loginController control = loader.getController();
			control.setMain(this);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Chat App-1.0");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void showChat(String ip, boolean isServer, String user) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/chatApp.fxml"));
			Parent root = loader.load();
			
			chatAppController control = (chatAppController) loader.getController();
			control.setMainApp(this, ip, isServer, user);	
			
			Stage chat = new Stage();
			chat.setResizable(false);
			chat.setTitle("Chat App-1.0");
			chat.setScene(new Scene(root));
			chat.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/login.fxml"));
			Parent root = loader.load();	
			loginController control = loader.getController();
			control.setMain(this);
			Stage chat = new Stage();
			chat.setScene(new Scene(root));
			chat.show();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
