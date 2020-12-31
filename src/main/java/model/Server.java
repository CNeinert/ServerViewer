package main.java.model;

//DB Test Object
public class Server {

	private String str_user, str_password, servername, bezeichnung, ip, os;
	
	private Integer int_id, int_port = 22;

	private Boolean enabled;

	public static Server getServerById(Integer int_id) {
		return new Server(int_id);
	}

	public Server() {

	}

	public Server(Integer int_id) {
		this.setId(int_id).load();
	}

	public Server load() {
		// TODO get data from DB

		switch (this.getId()) {
		case 1:
			this.setUser("test").setPassword("+-X+g#zDNeHYe!:F").setIp("161.97.73.151");
			this.setBezeichnung("Server 1");
			this.setServername("Server 1");
			this.setOs("Debian");
			this.setPort(22);
			break;
		case 2:
			this.setUser("astest").setPassword("=X(efR_#_zwj9wZ{").setIp("144.91.125.174");
			;
			this.setBezeichnung("Server 2");
			this.setServername("Server 2");
			this.setOs("Debian");
			this.setPort(22);
			break;
		}

		return this;
	}

	public Server save() {
		// TODO save data to DB
		return this;
	}

	public Integer getId() {
		return this.int_id;
	}

	public Server setId(Integer int_id) {
		this.int_id = int_id;
		return this;
	}

	public Integer getPort() {
		return this.int_port;
	}

	public Server setPort(Integer int_port) {
		this.int_port = int_port;
		return this;
	}


	public String getUser() {
		return this.str_user;
	}

	public Server setUser(String str_user) {
		this.str_user = str_user;
		return this;
	}

	public String getPassword() {
		return this.str_password;
	}

	public Server setPassword(String str_password) {
		this.str_password = str_password;
		return this;
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getHash() {
		return this.getPassword() + "|" + this.getUser() + "|" + Integer.toString(this.getId())
				+ "|" + Integer.toString(this.getPort());
	}
}
