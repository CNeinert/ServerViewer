package main.java.controller;

import com.jcraft.jsch.*;

import main.java.model.Program;
import main.java.model.Server;

import java.io.*;
import java.util.*;

public class ServerConnection {

	Server obj_server;
	String str_serverHash = "";
	Map<Object, Program> map_programVersions = new HashMap<>();
	Boolean boo_isConnectionFailed = false;

	public ServerConnection() {
		this.obj_server = new Server();
	}

	public ServerConnection(Integer int_id) {
		this.setServerById(int_id);
	}

	public ServerConnection(Server obj_server) {
		this.setServer(obj_server);
	}

	public Boolean isConfigurationValid() {
		if (!(this.getServerHash().equals(this.setServerHash()))) {
			if (this.execute("whoami", true).trim().equals(this.getServer().getUser())) {
				this.boo_isConnectionFailed = false;
			} else {
				this.boo_isConnectionFailed = true;
			}
		}
		return !this.boo_isConnectionFailed;
	}

	public String execute(String str_command) {
		return this.execute(str_command, false);
	}

	private String execute(String str_command, Boolean boo_ignoreConfigTest) {
		if (!this.isServerConfigured()) {
			return "ERROR: Server not configured";
		}
		if (!boo_ignoreConfigTest && !this.isConfigurationValid()) {
			return "ERROR: Server configuration not valid";
		}
		String str_return = "";

		try {
			JSch obj_jsch = new JSch();

			// Erstellung eines Verbindungsobjekts des Servers.
			Session obj_session = obj_jsch.getSession(this.getServer().getUser(), this.getServer().getIp(),
					this.getServer().getPort());
			obj_session.setPassword(this.getServer().getPassword());
			obj_session.setConfig("StrictHostKeyChecking", "no");
			obj_session.setTimeout(10000); // Nach 10 Sekunden wird der Verbindungsaufbau abgebrochen.
			obj_session.connect();

			// neue Shell session vorbereiten
			Channel obj_channel = obj_session.openChannel("exec");
			((ChannelExec) obj_channel).setCommand(str_command);
			obj_channel.setInputStream(null);
			// Errorchannel der Servershell
			ByteArrayOutputStream obj_byteOutputErrorStream = new ByteArrayOutputStream();
			((ChannelExec) obj_channel).setErrStream(obj_byteOutputErrorStream);
			// Ausgabe des Servers
			InputStream obj_inputStream = obj_channel.getInputStream();
			// Shell wird geöffnet
			obj_channel.connect();
			// WENN vom Server kein Error kommt...
			if (new String(obj_byteOutputErrorStream.toByteArray()).isEmpty()) {
				// ..dann lies die Antwort des Servers
				str_return = new String(obj_inputStream.readAllBytes());
			} else {
				// .. Sonst: Fehler
				str_return = "ERROR: " + new String(obj_byteOutputErrorStream.toByteArray());
			}
			obj_channel.disconnect();// shell beenden
			obj_session.disconnect();// session beenden
		} catch (Exception e) {
			str_return = "ERROR: Unable to communicate with server";
		}

		return str_return;
	}

	private Boolean isServerConfigured() {
		if (this.getServer().getPort() != 0 && this.getServer().getUser() != ""
				&& this.getServer().getPassword() != "") {
			return true;
		}
		return false;
	}

	private String getServerHash() {
		return this.str_serverHash;
	}

	private String setServerHash() {
		this.str_serverHash = this.getServer().getHash();
		return this.getServerHash();
	}

	private ServerConnection setServer(Server obj_server) {
		this.obj_server = obj_server;
		return this;
	}

	public Server getServer() {
		return this.obj_server;
	}

	public ServerConnection setServerById(Integer int_id) {
		this.setServer(Server.getServerById(int_id));
		return this;
	}

	public Program getProgramVersion(String str_program) {
		return this.getProgramVersions().get(str_program);
	}

	public Map<Object, Program> getProgramVersions(String[] arr_programs) {
		Map<Object, Program> obj_programVersions = new HashMap<>();
		for (String str_program : arr_programs) {
			obj_programVersions.put(str_program, this.getProgramVersions().get(str_program));
		}
		return obj_programVersions;
	}

	public Map<Object, Program> getProgramVersions() {
		if (!this.getServerHash().equals(this.getServer().getHash())
				|| (this.map_programVersions.isEmpty() && this.isServerConfigured() && this.isConfigurationValid())) {
			this.setProgramVersions();
		}
		return this.map_programVersions;
	}

	public ServerConnection setProgramVersions() {
		// TODO: Switch-case -> OS Unterscheidung
		String str_output = this.execute("apt list --installed 2>/dev/null");

		if (str_output.startsWith("ERROR")) {
			System.out.println(str_output);
			this.map_programVersions.clear();
			return this;
		}

		for (String str_line : str_output.split("\n")) {
			if (str_line.contains("/")) {
				String programString = new String();
				String progName = new String();
				String proVersion = new String();
				programString = str_line.substring(0, str_line.indexOf('/'));
				progName = str_line.split(" ")[0];
				proVersion = str_line.split(" ")[1];
				Program program = new Program(progName, proVersion);
				this.map_programVersions.put(program, program);
			}
		}

		return this;
	}

}