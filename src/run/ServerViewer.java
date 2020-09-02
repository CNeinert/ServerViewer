package run;

public class ServerViewer {
	
	public static void main(String[] args) {
		obj_connectionTest.getServer().setHost("test");
		
		controller.ServerConnection obj_connectionTest = new controller.ServerConnection(1);
		String[] arr_programsToCheck = {
			"xauth",
			"sudo",
			"screen"
		};
		System.out.println(obj_connectionTest.getProgramVersions(arr_programsToCheck));
	}
}