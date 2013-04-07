package com.cs307.groupnote;
public class User {
	
	private static User currentUser;

	private String username;
	private String sessioncode;
	
	private int refreshtime;
	
	private User() {
	}
	
	public static User getUser() { // Sets up singleton User class which allows only one active user at a time.
		if (currentUser == null) {
			currentUser = new User();
		}
		return currentUser;
	}
	
	public static Boolean alreadyLoggedIn() {
		Boolean active = false;
		
		if (currentUser != null) active = true;
		
		return active;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String setUsername(String user) {
		username = user;
		return username;
	}
	
	public String getSessionCode() {
		return sessioncode;
	}
	
	public String setSessionCode(String code) {
		sessioncode = code;
		return sessioncode;
	}
	
	public int getRefreshTime() {
		return refreshtime;
	}
	
	public int setRefreshTime(int refresh) {
		refreshtime = refresh;
		return refreshtime;
	}
	
	public void logoutUser () {
		currentUser = null;
	}
}
