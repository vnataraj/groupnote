package com.cs307.groupnote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.widget.Toast;

public class User {
	
	private static User currentUser;

	private String username;
	private String sessioncode;
	private String noteID; //the note_id of the users current note 
	private String currentSessionName; //the current session number the user is in
	private Integer currentSession; //the current session number the user is in
	private boolean otherNote;//if true then the user is looking at another users note, not their own. 
	private String otherName;//the name of the notes owner the user is looking at if not looking at their own.
	
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
	
	public String getCurrentSessionName() {
		return currentSessionName;
	}
	
	public String setCurrentSessionName(String name) {
		currentSessionName = name;
		return currentSessionName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String setUsername(String user) {
		username = user;
		return username;
	}
	public Integer getCurrentSession() {
		return currentSession;
	}
	
	public Integer setCurrentSession(Integer session) {
		currentSession = session;
		return currentSession;
	}
	public String getNoteID() {
		return noteID;
	}
	
	public String setNoteID(String note) {
		noteID = note;
		return noteID;
	}
	public boolean getOtherNote() {
		return otherNote;
	}
	
	public boolean setOtherNote(boolean note) {
		otherNote = note;
		return otherNote;
	}
	public String getOtherName() {
		return otherName;
	}
	
	public String setOtherName(String note) {
		noteID = note;
		return noteID;
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
		new updateRefreshTime().execute(Integer.toString(refresh));
		
		refreshtime = refresh;
		return refreshtime;
	}
	
	public void logoutUser () {
		currentUser = null;
	}
	
	
	private class updateRefreshTime extends AsyncTask<String, Void, Void> {
    	
 	   @Override
 	   protected Void doInBackground(String... params) {
 		   	   
 		   StringBuilder getRequest = new StringBuilder();
 		   
 		   getRequest.append("http://groupnote.net78.net/setRefreshTime.php?token=");
 		   getRequest.append(User.getUser().getSessionCode()); //Append user token here  <--------
 		   getRequest.append("&refresh_time=");
 		   getRequest.append(params[0]);
 		   
 	        try {
 	        	HttpClient client = new DefaultHttpClient();  
 	            URI getURL = new URI(getRequest.toString());
 	            HttpGet get = new HttpGet();
 	            get.setURI(getURL);
 	            HttpResponse responseGet = client.execute(get);  
 	        } 	    
 	        catch (Exception e) {
 	        	
 	        }
 	        
 	        return null;
 	   }
 	 
 }
}
