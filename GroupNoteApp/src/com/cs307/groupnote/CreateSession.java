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
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class CreateSession extends Activity {
	
	String[] userInfo = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_session);
        
    	final EditText usernametext = (EditText) findViewById(R.id.usernametext);
    	final EditText passwordtext = (EditText) findViewById(R.id.passcodetext);
    
        //create button
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	
            	if (usernametext.getText().toString().trim().equals("")) {
            		Toast toast = Toast.makeText(getApplicationContext(), "A session name is required!" , Toast.LENGTH_SHORT);
	    	        toast.show();
            	} else {
            		userInfo[0] = usernametext.getText().toString();
            		
            		/*if (passwordtext.getText().toString().trim().equals("")) {
            			userInfo[1] = "";
            		} else {
            			userInfo[1] = passwordtext.getText().toString().trim();
            		}*/
            		
            		userInfo[1] = passwordtext.getText().toString();
            		
            		userInfo[2] = "";
            		
            		new CreateNewSession().execute(userInfo);
            	}
            }
        });
    }

    private class CreateNewSession extends AsyncTask<String[], Void, String> {
    	
 	   @Override
 	   protected String doInBackground(String[]... params) {
 		   
 		   StringBuilder response = new StringBuilder();
 		   StringBuilder getRequest = new StringBuilder();
 		   
 		   getRequest.append("http://groupnote.net78.net/newSession.php?token=");
 		   getRequest.append(User.getUser().getSessionCode()); //Append user token here
 		   getRequest.append("&session_name=");
 		   getRequest.append(userInfo[0]); //Append session name here
 		   getRequest.append("&passcode=");
 		   getRequest.append(userInfo[1]); //Append session passcode here, optional.
 		   
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
 	       
 	       Toast toast = Toast.makeText(getApplicationContext(), result , Toast.LENGTH_SHORT);
        	toast.show();
 	        
 	        //Start new note activity
 	        Intent i = new Intent(getBaseContext(), ViewNote.class);
 	        startActivity(i);
 	   }
 	 
 } 
}
