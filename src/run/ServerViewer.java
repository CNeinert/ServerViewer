package run;

import java.util.Map;

public class ServerViewer {
	
	public static void main(String[] args) {
		
		
		System.out.print("Setup server connection... ");
		controller.ServerConnection obj_connectionTest = new controller.ServerConnection(1);
		System.out.println("done.");
		
		
		String[] arr_programsToCheck = {
			
			"sudo", "nano"
			
		};
		System.out.print("Start scanning... ");
		Map<String, String> scanresult = obj_connectionTest.getProgramVersions(arr_programsToCheck);
		System.out.println("done.");
		System.out.println(scanresult);
	}
}