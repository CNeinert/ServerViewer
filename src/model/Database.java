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
				+ "(name, ip, port, user, password, os) AS"
				+ "(?, ?, ?, ?, ?, ?);");
		buildState.setString(1, server.getName());
		buildState.setString(2, server.getIp());
		buildState.setInt(3, server.getPort());
		buildState.setString(4, server.getUser());
		buildState.setString(5, server.getPassword());
		buildState.setString(6, server.getOs());
		
		buildState.execute();
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
				+ "salt TEXT NOT NULL"
				+ ");");
		buildState.execute("CREATE TABLE Servers ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "servername TEXT NOT NULL,"
				+ "serverbezeichnung TEXT NOT NULL,"
				+ "ip TEXT NOT NULL,"
				+ "port INTEGER NOT NULL,"
				+ "user TEXT NOT NULL,"
				+ "password TEXT NOT NULL,"
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
