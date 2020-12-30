package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Program;
import model.Server;

public class OverviewController implements Initializable{
	
	Button SettingsButton = new Button();
	@FXML
	Label OneServerLabel = new Label(); 
	@FXML
	Label TwoServerLabel = new Label(); 
	@FXML
	Label ThreeServerLabel = new Label(); 
	@FXML
	Label FourServerLabel = new Label(); 
	@FXML
	Label FiveServerLabel = new Label(); 
	@FXML
	Label SixServerLabel = new Label(); 
	Label[] labelArray = new Label[6];
	
	
	@FXML
    private TableView<Program> OneServerTable, 
    						   TwoServerTable,
    						   ThreeServerTable,
    						   FourServerTable,
    						   FiveServerTable,
    						   SixServerTable = new TableView<Program>();
	/*
	@FXML
	private TableColumn<Program, String> programName;
	@FXML
	private TableColumn<Program, String> (SimpleStringProperty) programVersion;
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("INITIALIZE!");
		//programName.setCellValueFactory(new PropertyValueFactory<>("Program Name"));
		//programVersion.setCellValueFactory(new PropertyValueFactory<>("Version"));
		Server[] servers = DataController.getAllServers();
		ArrayList<Label> labelList = new ArrayList<Label>();
		labelList.add(OneServerLabel);
		labelList.add(TwoServerLabel);
		labelList.add(ThreeServerLabel);
		labelList.add(FourServerLabel);
		labelList.add(FiveServerLabel);
		labelList.add(SixServerLabel);
		
		ArrayList<TableView<Program>> tableViewList = new ArrayList<TableView<Program>>();
		tableViewList.add(OneServerTable);
		tableViewList.add(TwoServerTable);
		tableViewList.add(ThreeServerTable);
		tableViewList.add(FourServerTable);
		tableViewList.add(FiveServerTable);
		tableViewList.add(SixServerTable);
		
		
		int index = 0;
		for(TableView<Program> tab : tableViewList) {
			try {
				initTables(servers[index], tab, labelList.get(index));
				index++;
			}catch (Exception e) {
				break;
			}
			
		}
		
	}
	
	
	@FXML
	private void loadSettings(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
		Parent root = loader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.setTitle("Settings");
		stage.show();
	}

	public void showOverview() {
		
	}

	private void initTables(Server server, TableView<Program> tableView, Label serverLabel) {
		Program[] programs = DataController.getProgramsFromServer(server);
		
		TableColumn programNameCol = new TableColumn("Program");
	    programNameCol.setMinWidth(160);
	    programNameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("programName"));

	    TableColumn programVersionCol = new TableColumn("Version");
	    programVersionCol.setMinWidth(130);
	    programVersionCol.setCellValueFactory(new PropertyValueFactory<Program, String>("version"));

		
		
		//Casting the program Array to an ObservableList
		ObservableList<Program> programList = FXCollections.observableArrayList(programs);
		
		
		
		tableView.setItems(programList);
		tableView.getColumns().removeAll();
		tableView.getColumns().addAll(programNameCol, programVersionCol);
		
		serverLabel.setText(server.getIp());
	}
	
}
