package controller;

import java.sql.SQLException;

import jdk.internal.org.jline.reader.impl.history.DefaultHistory;
import model.Database;
import model.User;

public class Authentication {
	private Database db;
	public Authentication() {
		
		try {
			db = new Database();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean userExists(String username) {
		
		User user = db.getUser(username);
		if (user.getUsername().equals(username)) {
			return true;
		}
		return false;
	}
	
	public String saltPassword( String password, String salt) {
		return "";
	}
	
	public String generateSaltedPassword( String password) {
		return "";
	}
	
	public String hashPassword( String saltedPassword) {
		return "";
	}
	
	public boolean enterdPasswordIsCorrect(String enteredPassword, String dbPassword) {
		return false;
	}
	
	public boolean authUser(String username, String password) {
		//Get Database Object;
		
		//Check if the User exists
		if (!userExists(username)) {
			return false;
		}
		String saltedPassword = saltPassword(password, salt);
		String hashedPassword = hashPassword(saltedPassword);
		saltedPassword = new String();
		password = new String();
		//TODO Check hashed password with the one from the db
		if (true) {
			return true;
		}else {
			return false;
		}
		
		return false;
	}
	

}
