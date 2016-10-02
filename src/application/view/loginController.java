package application.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;


public class loginController{
	@FXML TextField userT, ipT;
	@FXML Label status;
	@FXML RadioButton rbH, rbG;
	private static boolean isHost;
	private Main main;
	
	public void handleConnect(ActionEvent e) throws SQLException {	
		if(ipT.getText()!=null){
		((Node)e.getSource()).getScene().getWindow().hide();
		if(isHost){main.showChat(null, true, userT.getText());}
		else{main.showChat(ipT.getText(), false, userT.getText());}
		} else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Wrong input");
			alert.setHeaderText("Try Again");	
			alert.show();
		}
	}
	
	public void handleRadioButton(ActionEvent e){
		if(rbH.isSelected()){
			ipT.setEditable(false);
			isHost = true;
		} else if(rbG.isSelected()){
			ipT.setEditable(true);
			isHost=false;
		}
	}
	public void handleExit(ActionEvent e) {
		System.exit(1);
	}

	public String getUserName() {
		return userT.getText();
	}

	public String getPassword() {
		return ipT.getText();
	}
	
	public void setMain(Main main){
		this.main = main;
	}

}
