package main.java.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.model.Program;
import main.java.model.Server;

public class SettingsController implements Initializable{

	
	@FXML
	private TableView<Server> TableServers = new TableView<Server>();
	
	@FXML
	private TextField txServerName, txUser, txPassword, txIp1,txIp2,txIp3,txIp4, txBezeichnung, txSsh = new TextField();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("INITIALIZE! SETTINGS");
		
		//saveNewServer("test", "+-X+g#zDNeHYe!:F", "Server 1", "Server 1", "161.97.73.151", 22);
		//saveNewServer("astest", "=X(efR_#_zwj9wZ{", "Server 2", "Server 2", "144.91.125.174", 22);
		initTable(TableServers);
		
	}
	
	public void showOverview() {

	}
	
	public void addNewServerButtonPressed() {
		String ipAddress = txIp1.getText()+"."+txIp2.getText()+"."+txIp3.getText()+"."+txIp4.getText();
		int port = Integer.parseInt(txSsh.getText());
		saveNewServer(txUser.getText(), txPassword.getText(), txServerName.getText(), txBezeichnung.getText(), ipAddress, port );
		initialize(null, null);
	}
	
	private void initTable(TableView<Server> tableView) {
		Server[] servers = DataController.getAllServers();

		TableColumn serverNameCol = new TableColumn("Servername");
		serverNameCol.setMinWidth(160);
		serverNameCol.setCellValueFactory(new PropertyValueFactory<Server, String>("servername"));
		
		TableColumn serverIpCol = new TableColumn("IP Addresse");
		serverIpCol.setMinWidth(130);
		serverIpCol.setCellValueFactory(new PropertyValueFactory<Server, String>("ip"));
		
		TableColumn serverDesciptionCol = new TableColumn("Description");
		serverDesciptionCol.setMinWidth(250);
		serverDesciptionCol.setCellValueFactory(new PropertyValueFactory<Server, String>("bezeichnung"));

		// Casting the program Array to an ObservableList
		ObservableList<Server> serverList = FXCollections.observableArrayList(servers);

		tableView.setItems(serverList);
		tableView.getColumns().removeAll();
		tableView.getColumns().addAll(serverNameCol, serverIpCol, serverDesciptionCol);

		
	}
	
	private void saveNewServer
	(String username, String password, String servername, String bezeichnung, String ip, int port)
	{
		Server newServer = new Server();
		newServer.setUser(username);
		newServer.setPassword(password);
		newServer.setServername(servername);
		newServer.setBezeichnung(bezeichnung);
		newServer.setIp(ip);
		newServer.setPort(port);
		newServer.setEnabled(true); //TODO REMOVE HARDCODED VALUE
		newServer.setOs("Debian based"); //TODO REMOVE HARDCODED VALUE
		
		DataController.SaveServer(newServer);
	}
}
