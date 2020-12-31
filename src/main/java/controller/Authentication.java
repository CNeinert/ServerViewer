package main.java.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import main.java.model.User;

public class Authentication {

	public String hashPassword(String password, String dbSalt) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-512");
			byte[] saltyBytes = dbSalt.getBytes(StandardCharsets.UTF_8);
			md.update(saltyBytes);
			byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
			return hashedPassword.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public String hashPassword(String password) {
		String salt = this.generateSalt();
		return hashPassword(password, salt);
	}

	public String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt.toString();
	}

	public boolean enterdPasswordIsCorrect(String enteredPassword, String dbPassword, String dbSalt) {
		String enteredPass = this.hashPassword(enteredPassword, dbSalt);
		return dbPassword.equals(enteredPass);
	}

	public boolean authUser(String username, String password) {
		// Get Database Object;
		User user = DataController.getUser(username);
		// Check if the User exists
		if (user.getUsername() != null) {
			return this.enterdPasswordIsCorrect(password, user.getPassword(), user.getSalt());
		} else {
			return false;
		}
	}

}
