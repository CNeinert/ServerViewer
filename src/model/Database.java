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
		//todo add params example drunter
		buildState.setString(1, server.getName());
		buildState.setString(2, server.getIp());
		buildState.setInt(3, server.getPort());
		buildState.setString(4, server.getUser());
		buildState.setString(5, server.getPassword());
		buildState.setString(6, server.getOs());
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
		buildState.execute("CREATE TABLE Servers ("
				+ "id integer NOT NULL PRIMARY KEY,"
				+ "name text,"
				+ "ip text not null,"
				+ "port int not null,"
				+ "user text not null,"
				+ "password text not null,"
				+ "os text not null"
				+ ");");
		buildState.execute("CREATE TABLE Programms ("
				+ "id integer not null primary key,"
				+ "server_fk integer not null,"
				+ "programm_name text not null,"
				+ "programm_version text not null,"
				+ "last_request text not null,"
				+ "foreign key(server_fk) references Servers(id)"
				+ ");");
	}
}
