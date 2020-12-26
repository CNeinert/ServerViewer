package run;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import controller.DataController;
import controller.ServerConnection;
import model.Program;
import model.Server;

public class ServerViewer {
	
	public static void main(String[] args) {
		
		System.out.print("Setup server connection... ");
		ServerConnection obj_connectionTest = new ServerConnection(1);
		System.out.println("done.");
		
		System.out.print("Start scanning... ");
		Map<Object, Program> scanresult = obj_connectionTest.getProgramVersions();
		System.out.println("done.");
		System.out.println(scanresult);
		
		
		Set<Entry<Object, Program>> st = scanresult.entrySet();
		int size = scanresult.size();
		int index = 1;
		
		Server thisServer = Server.getServerById(2);
		DataController.SaveServer(thisServer);
		
		 for( Entry<Object, Program> me:st)
		    {
			  Program output = me.getValue();
			  //System.out.println(output.getProgramName()+ "  -  "+output.getVersion());
			  DataController.SaveProgram(output, thisServer);
			  if(index % 10 == 0) {
				  System.out.println("Inserting Program "+index+"/"+size);
			  }
			  
			  index++;
		    }
		 
		 System.out.println("---------------------------------------------------");
		 System.out.println("|                 FROM DATABASE                   |");
		 System.out.println("---------------------------------------------------");
		 Program[] programms = DataController.getProgramsFromServer(thisServer);
		 System.out.println(programms[0].getProgramName());
		 for(Program i : programms) {
			 System.out.println(i.getProgramName()+" + "+i.getVersion());
		 }
	}
}
