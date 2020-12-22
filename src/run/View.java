package run;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class View {

    @FXML
    private Label label;
    
    @FXML
    private TableView<?> table_server_01;  

    public void initialize() {
    	
//    	System.out.print("Setup server connection... ");
//		controller.ServerConnection obj_connectionTest = new controller.ServerConnection(1);
//		System.out.println("done.");
//		
//		
//		String[] arr_programsToCheck = {
//			
//			"sudo", "nano"
//			
//		};
//		System.out.print("Start scanning... ");
//		Map<String, String> scanresult = obj_connectionTest.getProgramVersions(arr_programsToCheck);
//		System.out.println("done.");
//		System.out.println(scanresult);
    }
    
    public void showSettings(Stage primaryStage) throws Exception {
    	Parent root = FXMLLoader.load(getClass().getResource("/view/Settings.fxml"));
        primaryStage.setTitle("ServerViewer");
        primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(1250);
        Scene scene = new Scene(root, 1250, 700);
        scene.getStylesheets().add("/view/base.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    	
    }
    
    public void showOverview() {
    	
    }
}