package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// video von dem alles kopiert ist https://www.youtube.com/watch?v=JPsWaI5Z3gs

public class Database {
	private Connection con;
	private boolean hasData = false;
	
	public Database() throws ClassNotFoundException, SQLException {
		getConnection();
	}
	
	public Server insertServer(Server server) throws SQLException {
		PreparedStatement buildState = con.prepareStatement("INSERT INTO Servers"
				+ "(servername, serverbezeichnung, ip, port, user, password, os) AS"
				+ "(?, ?, ?, ?, ?, ?, ?);");
		buildState.setString(1, server.getName());
		buildState.setString(2, server.getBezeichnung());
		buildState.setString(3, server.getIp());
		buildState.setInt(4, server.getPort());
		buildState.setString(5, server.getUser());
		buildState.setString(6, server.getPassword());
		buildState.setString(7, server.getOs());
		
		buildState.execute();
	}
	
	public Server getServer(int id) throws SQLException {
		PreparedStatement buildState = con.prepareStatement("SELECT id, servername, serverbezeichnung, ip, port, user, password, os "
				+ "FROM Servers"
				+ "WHERE id = ?"
				+ "");
		buildState.setInt(1, id);		
		ResultSet result = buildState.executeQuery();
		result.next();
		
		Server rServer = new Server();			
		
		rServer.setId(result.getInt("id"));
		rServer.setName(result.getString("servername"));
		rServer.setBezeichnung(result.getString("serverbezeichnung"));
		rServer.setIp(result.getString("ip"));
		rServer.setPort(result.getInt("port"));
		rServer.setUser(result.getString("user"));
		rServer.setPassword(result.getString("password"));
		rServer.setOs(result.getString("os"));
		rServer.setEnabled(result.getInt("enabled"));
		
		return rServer;
	}
	
	public Server[] getServers() throws SQLException {
		
		PreparedStatement buildState = con.prepareStatement("SELECT id, servername, serverbezeichnung, ip, port, user, password, os "
				+ "FROM Servers");	
		ResultSet result = buildState.executeQuery();
				
		Server[] servers = new Server[result.getFetchSize()];
		
		int i = 0;
		while(result.next()) {
			servers[i] = new Server();
			
			servers[i].setId(result.getInt("id"));
			servers[i].setName(result.getString("servername"));
			servers[i].setBezeichnung(result.getString("serverbezeichnung"));
			servers[i].setIp(result.getString("ip"));
			servers[i].setPort(result.getInt("port"));
			servers[i].setUser(result.getString("user"));
			servers[i].setPassword(result.getString("password"));
			servers[i].setOs(result.getString("os"));
			servers[i].setEnabled(result.getInt("enabled"));
			
			i++;
		}
		return servers;
	}
	
	public Server updateServer(Server server) {
	
		PreparedStatement buildState = con.prepareStatement(""
				+ "UPDATE Servers SET"
				+ "servername = ?,"
				+ "serverbezeichnung = ?,"
				+ "ip = ?,"
				+ "port = ?,"
				+ "user= ?,"
				+ "password = ?,"
				+ "os = ?,"
				+ "enabled = ?"
				+ "WHERE id = ?"
				+ ";");
		buildState.setString(1, server.getName());
		buildState.setString(2, server.getBezeichnung());
		buildState.setString(3, server.getIp());
		buildState.setInt(4, server.getPort());
		buildState.setString(5, server.getUser());
		buildState.setString(6, server.getPassword());
		buildState.setString(7, server.getOs());
		buildState.setInt(8, server.getEnabled());
		buildState.setInt(9, server.getId());
		ResultSet result = buildState.executeQuery();
	return server;
	}
	
	public User insertUser(User user) {
		PreparedStatement buildState = con.prepareStatement("INSERT INTO Users"
				+ "(username, password, salt) AS"
				+ "(?, ?, ?);");
		buildState.setString(1, user.getUsername());
		buildState.setString(2, user.getPassword());
		buildState.setString(3, user.getSalt());
		
		buildState.execute();
		
		return user;
	}
	
	public User getUser(String username) {
		PreparedStatement buildState = con.prepareStatement("SELECT password, salt"
				+ "FROM Users"
				+ "WHERE username = ?"
				+ "");
		buildState.setString(1, username);		
		ResultSet result = buildState.executeQuery();
		result.next();
		
		Server rUser = new User();
		rUser.setUsername(result.getString("username"));
		rUser.setPassword(result.getString("password"));
		rUser.setSalt(result.getString("salt"));
		
		return rUser;
	}
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:ServerData.db");
		
		initialise();
	}
	
	private void initialise() throws SQLException {
		if(!hasData) 
			hasData = true;
		Statement state = con.createStatement();
		ResultSet result = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' and name = 'Servers';");
		
		if(!result.next()) {
			buildTables();
		}
		
	}
	
	private void buildTables() throws SQLException {
		System.out.println("Building the necessary tables");
		Statement buildState = con.createStatement();
		buildState.execute("CREATE TABLE Users ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "username TEXT NOT NULL,"
				+ "password TEXT NOT NULL,"
				+ "salt TEXT NOT NULL,"
				+ "CONSTRAINT unique_username UNIQUE (username)"
				+ ");");
		buildState.execute("CREATE TABLE Servers ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "servername TEXT NOT NULL,"
				+ "serverbezeichnung TEXT NOT NULL,"
				+ "ip TEXT NOT NULL,"
				+ "port INTEGER NOT NULL,"
				+ "user TEXT NOT NULL,"
				+ "password TEXT NOT NULL,"
				+ "enabled INTEGER NOT NULL DEFAULT 1"
				+ "os TEXT NOT NULL"
				+ ");");
		buildState.execute("CREATE TABLE Programs ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "program TEXT NOT NULL,"
				+ "version TEXT NOT NULL,"
				+ "last_request TEXT NOT NULL"
				+ ");");
		buildState.execute("CREATE TABLE Servers_Programs"
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "server_fk INTEGER NOT NULL,"
				+ "program_fk INTEGER NOT NULL,"
				+ "FOREIGN KEY (server_fk) REFERENCES Servers (id),"
				+ "FOREIGN KEY (program_fk) REFERENCES Programs (id)"
				+ ");");
	}
}
