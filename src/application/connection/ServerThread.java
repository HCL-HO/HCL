package application.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import application.Main;
import application.view.chatAppController;
import javafx.application.Platform;
import javafx.collections.ObservableList;

public class ServerThread extends Thread{
	private ServerConnectionThread mainServer;
	private Socket socket;
	private ObjectOutputStream out;
	private Consumer<Serializable> consume;
	public ServerThread(Socket clientSocket, Consumer<Serializable> consume) {
		this.socket = clientSocket;
		this.consume=consume;
	}
	public void run () {
		try {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			this.out = out;
			socket.setTcpNoDelay(true);
			out.writeObject("Connected to ChatRoom, hosted by: "+ ServerConnectionThread.UserName + "\n");
			chatAppController.InitailizeUserList(out);
			while(true){
				Serializable data = (Serializable) in.readObject();
				consume.accept(data);
				}
			}catch (Exception e) {
		}
	}

	public void sendMes(Serializable data) throws IOException{
			out.writeObject(data);

	}
	public void closeConnection() throws IOException{
		socket.close();
	}

	private void processData(Serializable data) {
		
	}
}
