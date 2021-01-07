package main.java.run;

import main.java.controller.DataController;
import main.java.model.Server;

public class GUIStarter {

	public static void main(String[] args) {
		Server server = new Server(1);
		DataController.SaveServer(server);
		Main.main(args);

	}

}
