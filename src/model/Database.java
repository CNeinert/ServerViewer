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
		buildState.setString(1, server.getServername());
		buildState.setString(2, server.getBezeichnung());
		buildState.setString(3, server.getIp());
		buildState.setInt(4, server.getPort());
		buildState.setString(5, server.getUser());
		buildState.setString(6, server.getPassword());
		buildState.setString(7, server.getOs());
		
		buildState.execute();
		
		return server;
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
		rServer.setServername(result.getString("servername"));
		rServer.setBezeichnung(result.getString("serverbezeichnung"));
		rServer.setIp(result.getString("ip"));
		rServer.setPort(result.getInt("port"));
		rServer.setUser(result.getString("user"));
		rServer.setPassword(result.getString("password"));
		rServer.setOs(result.getString("os"));
		
		if(result.getInt("enabled") == 1)
			rServer.setEnabled(true);
		else
			rServer.setEnabled(false);
		
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
			servers[i].setServername(result.getString("servername"));
			servers[i].setBezeichnung(result.getString("serverbezeichnung"));
			servers[i].setIp(result.getString("ip"));
			servers[i].setPort(result.getInt("port"));
			servers[i].setUser(result.getString("user"));
			servers[i].setPassword(result.getString("password"));
			servers[i].setOs(result.getString("os"));
			
			if(result.getInt("enabled") == 1)
				servers[i].setEnabled(true);
			else
				servers[i].setEnabled(false);
			
			i++;
		}
		return servers;
	}
	
	public Server updateServer(Server server) throws SQLException {
	
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
		buildState.setString(1, server.getServername());
		buildState.setString(2, server.getBezeichnung());
		buildState.setString(3, server.getIp());
		buildState.setInt(4, server.getPort());
		buildState.setString(5, server.getUser());
		buildState.setString(6, server.getPassword());
		buildState.setString(7, server.getOs());
		
		if(server.getEnabled())
			buildState.setInt(8, 1);
		else
			buildState.setInt(8, 0);
		
		buildState.setInt(9, server.getId());
		buildState.executeQuery();
	return server;
	}
	
	public User insertUser(User user) throws SQLException {
		PreparedStatement buildState = con.prepareStatement("INSERT INTO Users"
				+ "(username, password, salt) AS"
				+ "(?, ?, ?);");
		buildState.setString(1, user.getUsername());
		buildState.setString(2, user.getPassword());
		buildState.setString(3, user.getSalt());
		
		buildState.execute();
		
		return user;
	}
	
	public User getUser(String username) throws SQLException {
		PreparedStatement buildState = con.prepareStatement("SELECT password, salt"
				+ "FROM Users"
				+ "WHERE username = ?"
				+ ";");
		buildState.setString(1, username);		
		ResultSet result = buildState.executeQuery();
		result.next();
		
		User rUser = new User();
		rUser.setUsername(result.getString("username"));
		rUser.setPassword(result.getString("password"));
		rUser.setSalt(result.getString("salt"));
		
		return rUser;
	}
	
	public void insertProgramm(Program program, Server server) throws SQLException {
		if(!programExists(program)) {
			PreparedStatement buildStatePre = con.prepareStatement("INSERT INTO Programs"
					+ "(program, version, last_request) AS"
					+ "(?, ?, ?);");
			buildStatePre.setString(1, program.getProgramName());
			buildStatePre.setString(2, program.getVersion());
			buildStatePre.setString(3, program.getLastRequest());
			
			buildStatePre.execute();
		}
		PreparedStatement buildState = con.prepareStatement("INSERT INTO Programs_Servers"
				+ "(server_fk, program_fk) AS"
				+ "(("
				+ "SELECT id"
				+ "FROM programs"
				+ "WHERE program = ?"
				+ "AND version = ?"
				+ "), ("
				+ "SELECT id"
				+ "FROM servers"
				+ "WHERE ip = ?"
				+ "));");
		buildState.setString(1, program.getProgramName());
		buildState.setString(2, program.getVersion());
		buildState.setString(3, server.getIp());
		
		buildState.execute();		
	}
	
	private Boolean programExists(Program program) throws SQLException {
		PreparedStatement buildState = con.prepareStatement("SELECT id"
				+ "FROM Programs"
				+ "WHERE program = ?"
				+ "AND version = ?"
				+ ";");
		buildState.setString(1, program.getProgramName());
		buildState.setString(2, program.getVersion());
		ResultSet result = buildState.executeQuery();		
		
		return result.next();
	}
	
	public Program[] getProgramsFromServer(Server server) throws SQLException {
		PreparedStatement buildState = con.prepareStatement("SELECT program, version, last_request"
				+ "FROM Programs p"
				+ "INNER JOIN Servers_Programs sp"
				+ "ON p.id = sp.program_fk"
				+ "WHERE sp.server_fk = ("
				+ "	SELECT id"
				+ "	FROM servers"
				+ "	WHERE ip = ?"
				+ ")");
		buildState.setString(1, server.getIp());
		ResultSet result = buildState.executeQuery();
				
		Program[] programs = new Program[result.getFetchSize()];
		
		int i = 0;
		while(result.next()) {
			programs[i] = new Program();
			
			programs[i].setProgramName(result.getString("program"));			
			programs[i].setVersion(result.getString("version"));
			programs[i].setLastRequest(result.getString("last_request"));
			
			i++;
		}
		return programs;
	}

	public Server[] getServersFromProgram(Program program) throws SQLException {
		PreparedStatement buildState = con.prepareStatement("SELECT id, servername, serverbezeichnung, ip, port, user, password, os"
				+ "FROM servers s"
				+ "INNER JOIN Servers_Programs sp"
				+ "ON s.id = sp.server_fk"
				+ "WHERE sp.program_fk = ("
				+ "	SELECT id"
				+ "	FROM programs"
				+ "	WHERE program = ?"
				+ "	AND version = ?"
				+ ")");
		buildState.setString(1, program.getProgramName());
		buildState.setString(1, program.getVersion());
		ResultSet result = buildState.executeQuery();
				
		Server[] servers = new Server[result.getFetchSize()];
		
		int i = 0;
		while(result.next()) {
			servers[i] = new Server();
			
			servers[i].setId(result.getInt("id"));
			servers[i].setServername(result.getString("servername"));
			servers[i].setBezeichnung(result.getString("serverbezeichnung"));
			servers[i].setIp(result.getString("ip"));
			servers[i].setPort(result.getInt("port"));
			servers[i].setUser(result.getString("user"));
			servers[i].setPassword(result.getString("password"));
			servers[i].setOs(result.getString("os"));
			
			if(result.getInt("enabled") == 1)
				servers[i].setEnabled(true);
			else
				servers[i].setEnabled(false);
			
			i++;
		}
		return servers;
	}
	
	//TODO update last request
	
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
