package controller;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;

public class ServerConnection {

	model.Server obj_server;
	Map<String, String> map_programVersions = new HashMap<>();
	
	public ServerConnection() {
		this.obj_server = new model.Server();
	}
	
	public ServerConnection(Integer int_id) {
		this.setServerById(int_id);
	}
	
	public ServerConnection(model.Server obj_server) {
		this.setServer(obj_server);
	}
	
	public String execute(String str_command) {
		if(!this.isServerConfigured()) {
			return "ERROR: Server not configured";
		}
		String str_return = "";
		
		try {
			JSch obj_jsch = new JSch();
			
			Session obj_session = obj_jsch.getSession(this.getServer().getUser(), this.getServer().getHost(), this.getServer().getPort());
			obj_session.setPassword(this.getServer().getPassword());
			obj_session.setConfig("StrictHostKeyChecking", "no");
			obj_session.connect();
			
			Channel obj_channel = obj_session.openChannel("exec");
			((ChannelExec)obj_channel).setCommand(str_command);
			obj_channel.setInputStream(null);
			ByteArrayOutputStream obj_byteOutputErrorStream = new ByteArrayOutputStream();
			((ChannelExec)obj_channel).setErrStream(obj_byteOutputErrorStream);
			InputStream obj_inputStream = obj_channel.getInputStream();
			obj_channel.connect();
			if(new String(obj_byteOutputErrorStream.toByteArray()).isBlank()) {
				str_return = new String(obj_inputStream.readAllBytes());
			} else {
				str_return = "ERROR: " + new String(obj_byteOutputErrorStream.toByteArray());
			}
			obj_channel.disconnect();
			obj_session.disconnect();
		} catch(Exception e) {
			str_return = "ERROR: Unable to communicate with server";
		}
		
		return str_return;
	}
	
	private Boolean isServerConfigured() {
		if(
			this.getServer().getHost() != ""
			&& this.getServer().getPort() != 0
			&& this.getServer().getUser() != ""
			&& this.getServer().getPassword() != ""
		){
			return true;
		}
		return false;
	}
	
	private ServerConnection setServer(model.Server obj_server) {
		this.obj_server = obj_server;
		return this;
	}
	public model.Server getServer(){
		return this.obj_server;
	}
	public ServerConnection setServerById(Integer int_id) {
		this.obj_server = model.Server.getServerById(int_id);
		return this;
	}
	
	public String getProgramVersion(String str_program) {
		return this.getProgramVersions().get(str_program);
	}
	public Map<String, String> getProgramVersions(String[] arr_programs) {
		Map<String, String> obj_programVersions = new HashMap<>();
		for(String str_program : arr_programs) {
			obj_programVersions.put(str_program, this.getProgramVersions().get(str_program));
		}
		return obj_programVersions;
	}
	public Map<String, String> getProgramVersions() {
		if(this.map_programVersions.isEmpty()) {
			this.setProgramVersions();
		}
		return this.map_programVersions;
	}
	public ServerConnection setProgramVersions() {
		String str_output = this.execute("apt list --installed");
		
		if(str_output.startsWith("ERROR")) {
			System.out.println(str_output);
			return this;
		}
		
		for (String str_line : str_output.split("\n")) {
			if(str_line.contains("/")) {
				this.map_programVersions.put(str_line.substring(0, str_line.indexOf('/')), str_line.split(" ")[1]);
			}
		}
		
		return this;
	}
	
}