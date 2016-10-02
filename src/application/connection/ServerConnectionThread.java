package application.connection;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServerConnectionThread extends Thread{
	public static List<String> NameList = new ArrayList<String>();
	public static ArrayList<ServerThread> serversThreads = new ArrayList<>();
	private Consumer<Serializable> consume;
	public static String UserName;
	private int port;
	public ServerConnectionThread(int port, Consumer<Serializable> consume,String name){
		this.port=port;
		this.consume = consume;
		this.UserName = name;
	}
	@Override
	public void run(){
		try {
			ServerSocket server = new ServerSocket(port);
			while(true){
				Socket clientSocket = server.accept();
				ServerThread thread = new ServerThread(clientSocket, consume);
				serversThreads.add(thread);
				thread.start();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
