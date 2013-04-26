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

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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

public class JoinedSessions extends Activity {
	
	String[] classNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joined_sessions);  
        
        new PopulateJoinedSessions().execute("");
    }
    
    
    public void populateList(final String[] classes) {
    	// Find the ListView resource.   
    	ListView mainListView = (ListView) findViewById( R.id.listView1 );  
  
    	// Create and populate a List of class names.  
    	ArrayList<String> classList = new ArrayList<String>();  
    	classList.addAll( Arrays.asList(classNames) );
    
    	// Create ArrayAdapter using the class list.  
    	ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.listrow, classList);  
    
    	// Set the ArrayAdapter as the ListView's adapter.  
    	mainListView.setAdapter( listAdapter );
    
    	//Add click listener to list item
    	mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    		public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
    			// Perform action on click
    			String[] splits = classes[position].split(", ");
    			
    			User newuser = User.getUser();
	 	    	newuser.setCurrentSession(Integer.parseInt(splits[0]));
	 	    	newuser.setCurrentSessionName(splits[1]);
	 	    	
	 	    	if (Float.parseFloat(splits[2]) != 0 && Float.parseFloat(splits[3]) != 0) {
	 	    		//Check location here
	 	    		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	 	    		Criteria crit = new Criteria();
	 	    		Location location = lm.getLastKnownLocation(lm.getBestProvider(crit,true));
        		
	 	    		if (location == null) {
	 	    			Toast toast = Toast.makeText(getApplicationContext(), "Cannot retrive your location. Check your settings to continue." , Toast.LENGTH_LONG);
	 	    			toast.show();
	 	    			toast = Toast.makeText(getApplicationContext(), "Location required for entry!" , Toast.LENGTH_SHORT);
	 	    			toast.show();
	 	    			return;
	 	    		}
	 	    	
        		
	 	    		double longitude = location.getLongitude();
	 	    		double latitude = location.getLatitude();
	 	    		
	 	    		double longitudePlus = Float.parseFloat(splits[3]) + ((1.0/6.0) * Float.parseFloat(splits[4]));
	 	    		double latitudePlus = Float.parseFloat(splits[2]) + ((1.0/6.0) * Float.parseFloat(splits[4]));
	 	    		
	 	    		double longitudeMinus = Float.parseFloat(splits[3]) - ((1.0/6.0) * Float.parseFloat(splits[4]));
	 	    		double latitudeMinus = Float.parseFloat(splits[2]) - ((1.0/6.0) * Float.parseFloat(splits[4]));	
	 	    		
	 	    		/*Toast toast = Toast.makeText(getApplicationContext(), "(" + Double.toString(latitudeMinus) + "," + Double.toString(latitudePlus) + ") (" + Double.toString(longitudeMinus) + " , " + Double.toString(longitudePlus) + ")"  , Toast.LENGTH_LONG);
 	    			toast.show();*/
	 	    		
	 	    		if (longitude <= longitudePlus && longitude >= longitudeMinus && latitude <= latitudePlus && latitude >= latitudeMinus) {
	 	    			//Do nothing for now. Functionality can be added later.
	 	    		} else {
	 	    			Toast toast = Toast.makeText(getApplicationContext(), "Sorry, you are not within the allowed distance of this group. Entry not granted." , Toast.LENGTH_LONG);
	 	    			toast.show();
	 	    			return;
	 	    		}
	 	    		
	 	    	}
	 	    	
	 	    	Intent i = new Intent(getBaseContext(), ViewNote.class);
	    		startActivity(i);
    		}
    	}); 
    }
    
    
 public String[] translateServerResponse(String response) {
    	
    	int classCount = 0;
    	String[] classes;
    	String[] combined;
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
     	combined = new String[classCount];
     	classNames = new String[classCount];
     	
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
     		
     		combined[i] = Integer.toString(sectionId[i]) + ", " + classes[i];
     		
     		String[] temp2 = classes[i].split(", ");
     		
     		classNames[i] = temp2[0];
     	}
     	
     	return combined;
    }
    
    
    
    private class PopulateJoinedSessions extends AsyncTask<String, Void, String> {
    	
    	   @Override
    	   protected String doInBackground(String... params) {
    		   	   
    		   StringBuilder response = new StringBuilder();
    		   StringBuilder getRequest = new StringBuilder();
    		   
    		   getRequest.append("http://groupnote.net78.net/getSessions.php?token=");
    		   getRequest.append(User.getUser().getSessionCode()); //Append user token here  <--------
    		   getRequest.append("&saved=yes");
    		   
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
