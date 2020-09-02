package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseFunctions {
	private Connection con;
	private boolean hasData = false;
	
	public DatabaseFunctions() throws ClassNotFoundException, SQLException {
		getConnection();
	}
	
	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:ServerData.db");
		//DatabaseData.db is the name of the file if changed has to change here to
		// file doesn't exist currently
		
		initialise();
	}
	
	private void initialise() throws SQLException {
		if(!hasData) 
			hasData = true;
		Statement state = con.createStatement();
		ResultSet result = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' and name = 'Servers'");
		
		if(!result.next()) {
			buildTables();
		}
		
	}
	
	private void buildTables() throws SQLException {
		System.out.println("Building the necessary tables");
		Statement buildState = con.createStatement();
		buildState.execute("CREATE TABLE Servers ("
				+ "id integer NOT NULL PRIMARY KEY,"
				+ "servername text,"
				+ "ip text not null,"
				+ "port int not null,"
				+ "user text not null,"
				+ "password text not null,"
				+ "os text not null"
				+ ")");
		buildState.execute("CREATE TABLE Programms ("
				+ "id integer not null primary key,"
				+ "server_fk integer not null,"
				+ "programm_name text not null,"
				+ "programm_version text not null,"
				+ "last_request text not null,"
				+ "foreign key(server_fk) references Servers(id)"
				+ ")");
	}
}
