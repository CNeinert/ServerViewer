package run;

import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

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
    
    public void showSettings() {
    	
    }
    
    public void showOverview() {
    	
    }
}