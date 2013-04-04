package com.cs307.groupnote;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class FindSession extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_session);  
        
        new PopulateFindSession().execute("");
    }
    
    
    public void populateList(String[] classes) {
    	// Find the ListView resource.   
    	ListView mainListView = (ListView) findViewById( R.id.listView1 );  
  
    	// Create and populate a List of class names.  
    	ArrayList<String> classList = new ArrayList<String>();  
    	classList.addAll( Arrays.asList(classes) );
    
    	// Create ArrayAdapter using the class list.  
    	ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.listrow, classList);  
    
    	// Set the ArrayAdapter as the ListView's adapter.  
    	mainListView.setAdapter( listAdapter );
    
    	//Add click listener to list item
    	mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    		public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
    			// Perform action on click
    			Intent i = new Intent(getBaseContext(), EditNote.class);
    			startActivity(i);
    		}
    	});  
    }
    
    
    public String[] translateServerResponse(String response) {
    	
    	int classCount = 0;
    	String[] classes;
    	int[] sectionId;
    	
    	
    	/*Context context = getApplicationContext();
     	int duration = Toast.LENGTH_SHORT;
     	Toast toast = Toast.makeText(context, response, duration);
     	toast.show();*/
     	
     	/// Convert string to array of strings and integers containing the class names and the session id's consecutively.
     	
     	for (int i = 0; i < response.length(); i++) {
     		if (response.charAt(i) == ';') classCount++;
     	}
     	
     	classes = new String[classCount];
     	sectionId = new int[classCount];
     	
     	int counter = 0;
     	
     	for (int i = 0; i < classCount; i++) {
     		classes[i] = "";
     		sectionId[i] = -1;
     		
     		char tempchar;
 		   StringBuilder tempstring = new StringBuilder();
     		
     		while ((tempchar = response.charAt(counter++)) != ',') {
     			tempstring.append(Character.toString(tempchar));
     		}
 			sectionId[i] = Integer.parseInt(tempstring.toString());
     		
     		tempstring = new StringBuilder();
     		
     		while ((tempchar = response.charAt(counter++)) != ';') {
     			tempstring.append(Character.toString(tempchar));
     		}
     		classes[i] = tempstring.toString();
     		
     	}
     	
     	return classes;
    }
    
    
    
    private class PopulateFindSession extends AsyncTask<String, Void, String> {
    	
    	   @Override
    	   protected String doInBackground(String... params) {
    		   	   
    		   StringBuilder response = new StringBuilder();
    		   StringBuilder getRequest = new StringBuilder();
    		   
    		   getRequest.append("http://groupnote.net78.net/getSessions.php?token=");
    		   getRequest.append(User.getUser().getSessionCode()); //Append user token here
    		   getRequest.append("&saved=");
    		   
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
    	        populateList(translateServerResponse(result));
    	   }
    	 
    } 
}
