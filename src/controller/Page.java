package controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class Page {

	public static String str_filePathCss = "src/html/assets/css/";
	public static String str_filePathJs = "src/html/assets/js/";
	public static String str_filePathHtml = "src/html/";
	Map<String, String> map_programVersions = new HashMap<>();
	Map<String, String> map_css = new HashMap<>();
	Map<String, String> map_content = new HashMap<>();
	Map<String, String> map_js = new HashMap<>();
	Stage stage;
	
	public Page(Stage primaryStage) {
		this.stage = primaryStage;
		this
			.addCss("base.css")
			.addJs("base.js")
		;
	}
	
	public void show() {
		if(this.map_content.isEmpty()) {
			this.addHtml("base.html");
		}
		this.stage.setTitle("ServerViewer");
        WebView webView = new WebView();
        webView.getEngine().loadContent(this.toString());

        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 700, 400);

        stage.setScene(scene);
        stage.show();
	}
	
	public String toString() {
		String str_return = "<!DOCTYPE html><html><head>";
		for (Map.Entry<String, String> entry : this.map_js.entrySet()) {
			try {
				str_return += new String(Files.readAllBytes(Paths.get(entry.getValue())));
			} catch (IOException e) {
				e.printStackTrace();
				str_return += "";
			}
	    }
		str_return += "</head><body><style>";
		for (Map.Entry<String, String> entry : this.map_css.entrySet()) {
			try {
				str_return += new String(Files.readAllBytes(Paths.get(entry.getValue())));
			} catch (IOException e) {
				e.printStackTrace();
				str_return += "";
			}
		}
		str_return += "</style><div class=\"container\"><div class=\"left\">"+ 
				"	<div class=\"logo\">ServerViewer</div>\r\n" + 
				"	<div class=\"menu\">\r\n" + 
				"	<div class=\"menuitem hover\"><div class=\"pic\"><img src=\"assets/home.svg\"></div><div class=\"caption\">Home</div></div>\r\n" + 
				"	<div class=\"menuitem hover\"><div class=\"pic\"><img src=\"assets/server.svg\"></div><div class=\"caption\">Server</div></div>\r\n" + 
				"	<div class=\"menuitem hover\"><div class=\"pic\"><img src=\"assets/settings.svg\"></div><div class=\"caption\">Settings</div></div>\r\n" + 
				"	</div>\r\n" + 
				"</div>\r\n" + 
				"<div class=\"content\">";
		for (Map.Entry<String, String> entry : this.map_content.entrySet()) {
			try {
				str_return += new String(Files.readAllBytes(Paths.get(entry.getValue())));
			} catch (IOException e) {
				e.printStackTrace();
				str_return += "";
			}
		}
		str_return += "</div></div></body></html>";
		return str_return;
	}
	
	public Page addCss(String str_filename) {
		this.map_css.put(str_filename, Page.str_filePathCss+str_filename);
		return this;
	}
	
	public Page addJs(String str_filename) {
		this.map_css.put(str_filename, Page.str_filePathJs+str_filename);
		return this;
	}
	
	public Page addHtml(String str_filename) {
		this.map_css.put(str_filename, Page.str_filePathHtml+str_filename);
		return this;
	}
}