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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class Login extends Activity {
	
	String usernameTemp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        final EditText usernametext = (EditText) findViewById(R.id.usernametext);
        usernametext.requestFocus();
    	final EditText passwordtext = (EditText) findViewById(R.id.loginpasscodetext);
    	final CheckBox newUserBox = (CheckBox) findViewById(R.id.checkBox2);
        
        //create button
        final Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	if (!User.alreadyLoggedIn()) { //User is not currently logged in, this prevents unnecessary server calls.
            		//Do server connection call here...
            		//Set user information

            		if (usernametext.getText().toString().trim().equals("")) {
            			Toast toast = Toast.makeText(getApplicationContext(), "A username is required!" , Toast.LENGTH_SHORT);
    	    	        toast.show();
            		} else if (usernametext.getText().toString().trim().contains(" ")) {
            			Toast toast = Toast.makeText(getApplicationContext(), "The username may not contain spaces!" , Toast.LENGTH_SHORT);
    	    	        toast.show();
            		} else if (passwordtext.getText().toString().trim().equals("")) {
            			Toast toast = Toast.makeText(getApplicationContext(), "A password is required!" , Toast.LENGTH_SHORT);
    	    	        toast.show();
            		} else if (newUserBox.isChecked()) {
            			String[] userInfo = new String[2];
            			userInfo[0] = usernametext.getText().toString();
            			userInfo[1] = passwordtext.getText().toString();
            			usernameTemp = usernametext.getText().toString();
            			new NewUserServer().execute(userInfo);
            		} else {
            			String[] userInfo = new String[2];
            			userInfo[0] = usernametext.getText().toString();
            			userInfo[1] = passwordtext.getText().toString();
            			usernameTemp = usernametext.getText().toString();
            			new LoginServer().execute(userInfo);
            		}
            		
            	} else {
            		Intent i = new Intent(getBaseContext(), MainActivity.class);
            		startActivity(i);
            	}
            }
        });
    }
    
    public void onResume() { //Check if user is already logged in. If so let them in, there is no need to call the server again.
    	super.onResume();
    	if (User.alreadyLoggedIn()) { 
        	Intent i = new Intent(getBaseContext(), MainActivity.class);
    		startActivity(i);
        }
    }
    
    
    private class LoginServer extends AsyncTask<String[], Void, String> {
    	
	 	   @Override
	 	   protected String doInBackground(String[]... params) {
	 		   
	 		   StringBuilder response = new StringBuilder();
	 		   StringBuilder getRequest = new StringBuilder();
	 		   
	 		   getRequest.append("http://groupnote.net78.net/login.php?username=");
	 		   getRequest.append(params[0][0]); //Append user token here
	 		   getRequest.append("&password=");
	 		   getRequest.append(params[0][1].replace(" ", "+")); //Append session name here
	 		   
	 	        try {
	 	        	HttpClient client = new DefaultHttpClient();  
	 	            URI getURL = new URI(getRequest.toString());
	 	            HttpGet get = new HttpGet();
	 	            get.setURI(getURL);
	 	            HttpResponse responseGet = client.execute(get);  
	 	            HttpEntity resEntityGet = responseGet.getEntity(); 
	 	            if (resEntityGet != null) { 
	 	            	BufferedReader r = new BufferedReader(new InputStreamReader(resEntityGet.getContent()));
	 	            	String line;
	 	            	while ((line = r.readLine()) != null) {
	 	            	    response.append(line);
	 	            	}
	 	            }
	 	        } 	    
	 	        catch (Exception e) {
	 	        	runOnUiThread(new Runnable() {
	 	                public void run() {
	 	    	        	Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to server! Please try again." , Toast.LENGTH_SHORT);
	 	    	        	toast.show();
	 	                }
	 	        	});
	 	        }
	 	        
	 	        return response.toString();
	 	   }
	 	   
	 	   @Override
	 	   protected void onPostExecute(String result)  {
	 	       super.onPostExecute(result);
	 	       
	 	       if (result.trim().equals("-1")) {
	 	    	   	Toast toast = Toast.makeText(getApplicationContext(), "Incorrect user name or password. Please try again." , Toast.LENGTH_SHORT);
   	        		toast.show();
	 	       } else {
	 	       
	 	    	   //User class creation and login take place here
	 	    	   User newuser = User.getUser();
	 	    	   newuser.setUsername(usernameTemp);
	 	    	   newuser.setSessionCode(result);
	 	    	   new getRefreshTime().execute("");
       		
	 	    	   Intent i = new Intent(getBaseContext(), MainActivity.class);
	 	    	   startActivity(i);
	 	    	   
	 	    	   final EditText usernametext = (EditText) findViewById(R.id.usernametext);
	 	    	   usernametext.setText("");
	 	    	   usernametext.requestFocus();
	 	    	   final EditText passwordtext = (EditText) findViewById(R.id.loginpasscodetext);
	 	    	   passwordtext.setText("");
	 	    	   final CheckBox newUserBox = (CheckBox) findViewById(R.id.checkBox2);
	 	    	   newUserBox.setChecked(false);
	 	    	   
	 	    	   Toast toast = Toast.makeText(getApplicationContext(), ("Welcome back, " + usernameTemp + "!") , Toast.LENGTH_SHORT);
	        	   toast.show();
	 	       }
	 	        
	 	   }
	 	 
	 }
    
    private class NewUserServer extends AsyncTask<String[], Void, String> {
    	
	 	   @Override
	 	   protected String doInBackground(String[]... params) {
	 		   
	 		   StringBuilder response = new StringBuilder();
	 		   StringBuilder getRequest = new StringBuilder();
	 		   
	 		   getRequest.append("http://groupnote.net78.net/newUser.php?username=");
	 		   getRequest.append(params[0][0]); //Append user token here
	 		   getRequest.append("&password=");
	 		   getRequest.append(params[0][1].replace(" ", "+")); //Append session name here
	 		   
	 	        try {
	 	        	HttpClient client = new DefaultHttpClient();  
	 	            URI getURL = new URI(getRequest.toString());
	 	            HttpGet get = new HttpGet();
	 	            get.setURI(getURL);
	 	            HttpResponse responseGet = client.execute(get);  
	 	            HttpEntity resEntityGet = responseGet.getEntity(); 
	 	            if (resEntityGet != null) { 
	 	            	BufferedReader r = new BufferedReader(new InputStreamReader(resEntityGet.getContent()));
	 	            	String line;
	 	            	while ((line = r.readLine()) != null) {
	 	            	    response.append(line);
	 	            	}
	 	            }
	 	        } 	    
	 	        catch (Exception e) {
	 	        	runOnUiThread(new Runnable() {
	 	                public void run() {
	 	    	        	Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to server! Please try again." , Toast.LENGTH_SHORT);
	 	    	        	toast.show();
	 	                }
	 	        	});
	 	        }
	 	        
	 	        return response.toString();
	 	   }
	 	   
	 	   @Override
	 	   protected void onPostExecute(String result)  {
	 	       super.onPostExecute(result);
	 	       
	 	       if (result.equals("-1")) {
	 	    	   	Toast toast = Toast.makeText(getApplicationContext(), "New user creation encountered a problem. This username may already be in use." , Toast.LENGTH_LONG);
	        		toast.show();
	 	       } else {
	 	    	   //User class creation and login take place here
	 	    	   User newuser = User.getUser();
	 	    	   newuser.setUsername(usernameTemp);
	 	    	   newuser.setSessionCode(result);
	 	    	   newuser.setRefreshTime(30);
    		
	 	    	   Intent i = new Intent(getBaseContext(), MainActivity.class);
	 	    	   startActivity(i);
	 	    	   
	 	    	   final EditText usernametext = (EditText) findViewById(R.id.usernametext);
	 	    	   usernametext.setText("");
	 	    	   usernametext.requestFocus();
	 	    	   final EditText passwordtext = (EditText) findViewById(R.id.loginpasscodetext);
	 	    	   passwordtext.setText("");
	 	    	   final CheckBox newUserBox = (CheckBox) findViewById(R.id.checkBox2);
	 	    	   newUserBox.setChecked(false);
	 	    	   
	 	    	   Toast toast = Toast.makeText(getApplicationContext(), ("Welcome new user, " + usernameTemp + "!") , Toast.LENGTH_SHORT);
	        	   toast.show();
	 	       }
	 	        
	 	   }
	 	 
	 }
    
    
    
    
    private class getRefreshTime extends AsyncTask<String, Void, String> {
    	
 	   @Override
 	   protected String doInBackground(String... params) {
 		   	   
 		   StringBuilder response = new StringBuilder();
 		   StringBuilder getRequest = new StringBuilder();
 		   
 		   getRequest.append("http://groupnote.net78.net/getRefreshTime.php?token=");
 		   getRequest.append(User.getUser().getSessionCode()); //Append user token here  <--------
 		   
 	        try {
 	        	HttpClient client = new DefaultHttpClient();  
 	            URI getURL = new URI(getRequest.toString());
 	            HttpGet get = new HttpGet();
 	            get.setURI(getURL);
 	            HttpResponse responseGet = client.execute(get);  
 	            HttpEntity resEntityGet = responseGet.getEntity(); 
 	            if (resEntityGet != null) { 
 	            	BufferedReader r = new BufferedReader(new InputStreamReader(resEntityGet.getContent()));
 	            	String line;
 	            	while ((line = r.readLine()) != null) {
 	            	    response.append(line);
 	            	}
 	            }
 	        } 	    
 	        catch (Exception e) {
 	        	runOnUiThread(new Runnable() {
 	                public void run() {
 	    	        	Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to server! Please try again." , Toast.LENGTH_SHORT);
 	    	        	toast.show();
 	                }
 	        	});
 	        }
 	        
 	        return response.toString();
 	   }
 	   
 	   @Override
 	   protected void onPostExecute(String result)  {
 	        super.onPostExecute(result);
 	        User newuser = User.getUser();
 	        newuser.setRefreshTime(Integer.parseInt(result.trim()));
 	   }
 	 
 } 
}


