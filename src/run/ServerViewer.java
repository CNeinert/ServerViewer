package run;

import controller.Page;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerViewer extends Application{

	public void start(Stage primaryStage) {
		controller.Page page = new Page(primaryStage);
		page.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
