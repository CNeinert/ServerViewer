package main.java.run;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.controller.DataController;
import main.java.controller.ServerConnection;
import main.java.model.Server;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/Overview.fxml"));
		primaryStage.setTitle("ServerViewer");
		primaryStage.setMinHeight(700);
		primaryStage.setMinWidth(1250);
		Scene scene = new Scene(root, 1250, 700);
		scene.getStylesheets().add("view/base.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Server server = new Server(1);
		DataController.SaveServer(server);
		launch(args);
	}
}