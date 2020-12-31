package main.java.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//TODO lösch funktionen schreiben für ggf Program und Server
// nicht vergessen es muss auch aus der Servers_Programs tabelle gelöscht werden
// video von dem alles kopiert ist https://www.youtube.com/watch?v=JPsWaI5Z3gs

public class Database {
	private Connection con;
	private boolean hasData = false;

	public Database() throws ClassNotFoundException, SQLException {
		getConnection();
	}

	private void initialise() throws SQLException {
		if (!hasData)
			hasData = true;
		Statement state = con.createStatement();
		ResultSet result = state
				.executeQuery("SELECT name FROM sqlite_master WHERE type='table' and name = 'Servers';");
		System.out.println("SQL RESULT: " + result.toString());
		if (!result.next()) {
			buildTables();
		}
	}

	public Server insertServer(Server server) throws SQLException {
		if (isExistingServer(server)) {
			return server;
		}
		PreparedStatement buildState = con.prepareStatement("INSERT INTO Servers "
				+ "(servername, serverbezeichnung, ip, port, user, password, os) VALUES " + "(?, ?, ?, ?, ?, ?, ?);");
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

	public Boolean isExistingServer(Server server) {

		try {
			PreparedStatement buildState = con.prepareStatement("SELECT 1 FROM Servers WHERE ip = ?;");
			buildState.setString(1, server.getIp());
			ResultSet result = buildState.executeQuery();
			if (result.next()) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	public Server getServer(int id) throws SQLException {
		PreparedStatement buildState = con
				.prepareStatement("SELECT id, servername, serverbezeichnung, ip, port, user, password, os "
						+ "FROM Servers " + "WHERE id = ?" + ";");
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

		if (result.getInt("enabled") == 1)
			rServer.setEnabled(true);
		else
			rServer.setEnabled(false);

		return rServer;
	}

	public Server[] getServers() throws SQLException {

		PreparedStatement buildState = con.prepareStatement(
				"SELECT id, servername, serverbezeichnung, ip, port, user, password, os, enabled " + "FROM Servers;");
		ResultSet result = buildState.executeQuery();

		Server[] servers = new Server[0];
		ArrayList<Server> serverList = new ArrayList<Server>();
		int i = 0;
		while (result.next()) {
			Server aserver = new Server();

			aserver.setId(result.getInt("id"));
			aserver.setServername(result.getString("servername"));
			aserver.setBezeichnung(result.getString("serverbezeichnung"));
			aserver.setIp(result.getString("ip"));
			aserver.setPort(result.getInt("port"));
			aserver.setUser(result.getString("user"));
			aserver.setPassword(result.getString("password"));
			aserver.setOs(result.getString("os"));

			if (result.getInt("enabled") == 1)
				aserver.setEnabled(true);
			else
				aserver.setEnabled(false);

			serverList.add(aserver);
			servers = serverList.toArray(servers);

			i++;
		}

		return servers;
	}

	public Server updateServer(Server server) throws SQLException {

		PreparedStatement buildState = con.prepareStatement(
				"" + "UPDATE Servers SET " + "servername = ?, " + "serverbezeichnung = ?, " + "ip = ?, " + "port = ?, "
						+ "user= ?, " + "password = ?, " + "os = ?, " + "enabled = ? " + "WHERE id = ?" + ";");
		buildState.setString(1, server.getServername());
		buildState.setString(2, server.getBezeichnung());
		buildState.setString(3, server.getIp());
		buildState.setInt(4, server.getPort());
		buildState.setString(5, server.getUser());
		buildState.setString(6, server.getPassword());
		buildState.setString(7, server.getOs());

		if (server.getEnabled())
			buildState.setInt(8, 1);
		else
			buildState.setInt(8, 0);

		buildState.setInt(9, server.getId());
		buildState.executeQuery();
		return server;
	}

	public User insertUser(User user) throws SQLException {
		PreparedStatement buildState = con
				.prepareStatement("INSERT INTO Users " + "(username, password, salt) VALUES " + "(?, ?, ?);");
		buildState.setString(1, user.getUsername());
		buildState.setString(2, user.getPassword());
		buildState.setString(3, user.getSalt());

		buildState.execute();

		return user;
	}

	public User getUser(String username) throws SQLException {
		PreparedStatement buildState = con
				.prepareStatement("SELECT password, salt " + "FROM Users " + "WHERE username = ?" + ";");
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
		if (server.getIp() == null) {
			System.out.println("NO SERVER IP");
			System.out.println(server.getServername());
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());

		if (!programExists(program)) {
			PreparedStatement buildStatePre = con
					.prepareStatement("INSERT INTO Programs " + " (program, version) VALUES " + "(?, ?)");
			buildStatePre.setString(1, program.getProgramName());
			buildStatePre.setString(2, program.getVersion());

			buildStatePre.execute();
		}
		PreparedStatement buildState = con.prepareStatement("INSERT INTO Servers_Programs (server_fk, program_fk) "
				+ "SELECT s.id, p.id " + "FROM servers AS s " + "CROSS JOIN programs AS p " + "WHERE s.ip = ? "
				+ "AND p.program = ? " + "AND p.version = ?" + ";");
		buildState.setString(1, server.getIp().toString());
		buildState.setString(2, program.getProgramName());
		buildState.setString(3, program.getVersion());

		buildState.execute();
	}

	private Boolean programExists(Program program) throws SQLException {
		PreparedStatement buildState = con
				.prepareStatement("SELECT * " + "FROM Programs " + "WHERE program = ? " + "AND version = ?" + ";");
		buildState.setString(1, program.getProgramName());
		buildState.setString(2, program.getVersion());
		ResultSet result = buildState.executeQuery();
		return result.next();
	}

	// Gets all Programs of a single Server from the Database
	public Program[] getProgramsFromServer(Server server) throws SQLException {

		PreparedStatement buildState = con.prepareStatement("SELECT program, version FROM Programs AS p "
				+ "INNER JOIN Servers_Programs AS sp ON p.id = sp.program_fk "
				+ "WHERE sp.server_fk = (SELECT id FROM servers WHERE ip = ?);");
		buildState.setString(1, server.getIp());

		ResultSet result = buildState.executeQuery();
		// System.out.println(result.first());
		Program[] programArray = new Program[0];
		ArrayList<Program> programList = new ArrayList<Program>();
		int i = 0;
		while (result.next()) {
			Program program = new Program();

			program.setProgramName(result.getString("program"));
			program.setVersion(result.getString("version"));
			programList.add(program);
			programArray = programList.toArray(programArray);

			i++;
		}
		return programArray;

	}

	// Get all Servers where the specified Program is installed
	public Server[] getServersFromProgram(Program program) throws SQLException {
		PreparedStatement buildState = con.prepareStatement(
				"SELECT id, servername, serverbezeichnung, ip, port, user, password, os " + "FROM servers s "
						+ "INNER JOIN Servers_Programs sp " + "ON s.id = sp.server_fk " + "WHERE sp.program_fk = ("
						+ "	SELECT id " + "	FROM programs " + "	WHERE program = ? " + "	AND version = ?" + ")");
		buildState.setString(1, program.getProgramName());
		buildState.setString(1, program.getVersion());
		ResultSet result = buildState.executeQuery();

		Server[] servers = new Server[result.getFetchSize()];

		int i = 0;
		while (result.next()) {
			servers[i] = new Server();

			servers[i].setId(result.getInt("id"));
			servers[i].setServername(result.getString("servername"));
			servers[i].setBezeichnung(result.getString("serverbezeichnung"));
			servers[i].setIp(result.getString("ip"));
			servers[i].setPort(result.getInt("port"));
			servers[i].setUser(result.getString("user"));
			servers[i].setPassword(result.getString("password"));
			servers[i].setOs(result.getString("os"));

			if (result.getInt("enabled") == 1)
				servers[i].setEnabled(true);
			else
				servers[i].setEnabled(false);

			i++;
		}
		return servers;
	}

	private void getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:ServerData.db");

		initialise();
	}

	private void buildTables() throws SQLException {
		System.out.println("Building the necessary tables");
		Statement buildState = con.createStatement();
		buildState.execute("CREATE TABLE Users (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "username TEXT NOT NULL, " + "password TEXT NOT NULL, " + "salt TEXT NOT NULL, "
				+ "CONSTRAINT unique_username UNIQUE (username)" + ");");
		buildState.execute("CREATE TABLE Servers (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "servername TEXT NOT NULL, " + "serverbezeichnung TEXT NOT NULL, " + "ip TEXT NOT NULL, "
				+ "port INTEGER NOT NULL, " + "user TEXT NOT NULL, " + "password TEXT NOT NULL, "
				+ "enabled INTEGER NOT NULL DEFAULT 1, " + "os TEXT NOT NULL" + ");");
		buildState.execute("CREATE TABLE Programs (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "program TEXT NOT NULL, " + "version TEXT NOT NULL " + ");");
		buildState.execute("CREATE TABLE Servers_Programs (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "server_fk INTEGER NOT NULL, " + "program_fk INTEGER NOT NULL, "
				+ "FOREIGN KEY (server_fk) REFERENCES Servers (id), "
				+ "FOREIGN KEY (program_fk) REFERENCES Programs (id)" + ");");
	}
}
