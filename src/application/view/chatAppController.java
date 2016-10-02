package application.view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import application.connection.ClientThread;
import application.connection.ServerConnectionThread;
import application.connection.ServerThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sun.security.jca.GetInstance;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class chatAppController implements Initializable {
	private Main main;
	@FXML
	private TextArea message;
	@FXML
	private ListView<String> chatList;
	@FXML
	private TextField input;
	@FXML 
	private Label welcome;
	
	//public static List<String> nameList = new ArrayList<String>();
	public static ObservableList<String> nameList = FXCollections.observableArrayList();
	private ListProperty<String> listProperty = new SimpleListProperty<String>(); 
	public static boolean newUserJoined = false;
	private static String userName;
	private static String ip;
	private static boolean isServer;
	private int port = 54561;
	private ClientThread client;
	private Consumer<Serializable> serverConsume;
	private Consumer<Serializable> clientConsume;
	
	public void setMainApp(Main main2, String ip, boolean isServer, String userName) {
		this.main = main2;
		this.ip = ip;
		this.isServer = isServer;
		this.userName = userName;
		welcome.setText("Welcome: "+ userName);
		if(isServer){
			ServerConnectionThread thread = new ServerConnectionThread(port, serverConsume, userName);
			thread.start();
		}else{
			client = new ClientThread(ip, port, clientConsume, userName);
			client.start();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		message.setEditable(false);
		listProperty.set(nameList);
		chatList.itemsProperty().bind(listProperty);
		serverConsume = data -> {
			if(data.toString().contains(Main.CODE_NAME)){
				addToUserList(data);
			} else if(data.toString().contains(Main.Logout_Name_code)){
				removeThread(data);
				deleteFromUserList(data);
			} else {
			Platform.runLater(()->{
				message.appendText(data.toString());
			});
		}	try{sendMesFromServer(data.toString());
			} catch(IOException e){};
			}; 
		
		clientConsume = data -> {
			if(data.toString().contains(Main.CODE_NAME)){
				addToUserList(data);
			} else if(data.toString().contains(Main.Logout_Name_code)){
				deleteFromUserList(data);
			} else {
			Platform.runLater(()->{
				message.appendText(data.toString());
			});
			}
		}; 
	}

@FXML public void handleInput(ActionEvent e) throws IOException{
		String mes = input.getText();
		String sendMes = userName+": "+ mes+"\n";
		input.clear();
		if(isServer){
			message.appendText(sendMes);
			sendMesFromServer(sendMes);
		} else {
			client.sendMes(sendMes);
			System.out.println("mes sent");
		}		
	}

		@FXML public void handleClear(ActionEvent e){
			chatList.getItems().removeAll();
		}
		@FXML public void handleLogout(ActionEvent e) throws IOException{
		if(isServer){
			for(int i=0;i<ServerConnectionThread.serversThreads.size();i++){
				ServerConnectionThread.serversThreads.get(i).sendMes("Server has closed, bye!");
				ServerConnectionThread.serversThreads.get(i).closeConnection();;
			}
		} else {client.logOut();}
		System.exit(1);
		}
		
		public void sendMesFromServer(String sendMes) throws IOException{
			for(int i=0;i<ServerConnectionThread.serversThreads.size();i++){
				ServerConnectionThread.serversThreads.get(i).sendMes(sendMes);
			}
		}

		private void addToUserList(Serializable data) {
			Platform.runLater(()->{
				String user = data.toString().replace(Main.CODE_NAME, "");
				nameList.add(user);
			});			
		}
		private void deleteFromUserList(Serializable data) {
			Platform.runLater(()->{
				String user = data.toString().replace(Main.Logout_Name_code, "");
				boolean remove = nameList.remove(user);
				System.out.println(user+" "+ remove);
				System.out.println(nameList);
			});						
		}
		
		private void removeThread(Serializable data) {
				String user = data.toString().replace(Main.Logout_Name_code, "");
				int indexofthread = nameList.indexOf(user);
				ServerConnectionThread.serversThreads.remove(indexofthread);
			}
		public static void InitailizeUserList(ObjectOutputStream out) throws IOException {
			for(int i =0; i<nameList.size();i++){
				out.writeObject(Main.CODE_NAME+ nameList.get(i));
			}
		}
}
