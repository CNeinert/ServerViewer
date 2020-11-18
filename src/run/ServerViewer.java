package run;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import controller.DataController;
import model.Program;
import model.Server;

public class ServerViewer {
	
	public static void main(String[] args) {
		
		
		System.out.print("Setup server connection... ");
		controller.ServerConnection obj_connectionTest = new controller.ServerConnection(1);
		System.out.println("done.");
		
		
		String[] arr_programsToCheck = {
			
			"sudo", "nano"
			
		};
		System.out.print("Start scanning... ");
		Map<Object, Program> scanresult = obj_connectionTest.getProgramVersions();
		System.out.println("done.");
		System.out.println(scanresult);
		
		
		
		
		
		Set<Entry<Object, Program>> st = scanresult.entrySet();
		 for( Entry<Object, Program> me:st)
		    {
			  Program output = me.getValue();
			  System.out.println(output.getProgramName()+ "  -  "+output.getVersion());
			  DataController.SaveProgram(output, Server.getServerById(1));
			 
		    }
	}
}
