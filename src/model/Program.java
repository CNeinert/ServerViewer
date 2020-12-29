package model;

public class Program {
	
	private String programName;
	private String version;
	
	
	public Program() {	}
	
	public Program(String Name, String Version) {
		this.setProgramName(Name);
		this.setVersion(Version);
	}
	

	public String getProgramName() {
		return programName;
	}


	public void setProgramName(String programName) {
		this.programName = programName;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}

}
