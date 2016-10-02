package application.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;

import application.Main;
import application.view.chatAppController;
import javafx.collections.ObservableList;

public class ClientThread extends Thread{
	private Socket socket;
	private ObjectOutputStream out;
	public static String UserName;
	private Consumer<Serializable> consume;
	public ClientThread(String ip, int port,Consumer<Serializable> consume, String UserName) {
		this.UserName = UserName;
		this.consume = consume;
		try {
			socket = new Socket(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run () {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			this.out = out;
			socket.setTcpNoDelay(true);
			out.writeObject("("+UserName+")"+" has joined the room"+"\n");
			out.writeObject(Main.CODE_NAME+UserName);
			while(true){
				Serializable data = (Serializable) in.readObject();
				consume.accept(data);				}
			}catch (Exception e) {
		}
	}
	
	public void sendMes(Serializable data){
		try {
			out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void logOut() throws IOException{
		sendMes(Main.Logout_Name_code + UserName);
		sendMes(UserName+ " has left the room"+"\n");
		closeConnection();
	}
	public void closeConnection() throws IOException{
		socket.close();
	}
}
