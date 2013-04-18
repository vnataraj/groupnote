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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class FindSession extends Activity {
	
	String[] combined;
	int classCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_session);  
        
        new PopulateFindSession().execute("");
        
        final EditText et = (EditText) findViewById(R.id.notetext);
        
        et.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				filterListResults(et.getText().toString());
			}
        	
    	});
    }
    
    public void filterListResults(String filter) {
    	
    	if (filter.equals("")) {
    		populateList(combined);
    	}
    	
    	String[] filtered = new String[classCount];
    	
    	int counter = 0;
    	
    	for (int i = 0; i < classCount; i++) {
    		if (combined[i].toLowerCase().contains(filter.toLowerCase())) {
    			filtered[counter++] = combined[i];
    		}
    	}
    	
    	//copy array to array of proper size
    	
    	String[] truncated = new String[counter];
    	
    	for (int i = 0; i < counter; i++) {
    		truncated[i] = filtered[i];
    	}
    	
    	populateList(truncated);
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
    			String[] splits = adapter.getItemAtPosition(position).toString().split(", ");
    			
    			User newuser = User.getUser();
	 	    	newuser.setCurrentSession(Integer.parseInt(splits[0]));
	 	    	newuser.setCurrentSessionName(splits[1]);
    			new NewNote().execute(""); //creates a new note for the user in this session
    			Intent i = new Intent(getBaseContext(), EditNote.class);
    			startActivity(i);
    		}
    	});   
    }
    
 public String[] translateServerResponse(String response) {
    	
    	classCount = 0;
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
     	combined = new String[classCount];
     	
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
     	}
     	
     	return combined;
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
    
	   private class NewNote extends AsyncTask<String,Void,String>
	   {
		   @Override
		   protected String doInBackground(String...params)
		   {
			   //Get the new Session ID
	 		   StringBuilder response = new StringBuilder();
	 		   StringBuilder getRequest = new StringBuilder();
	 		   
	 		   
	 		   getRequest.append("http://groupnote.net78.net/getSessions.php?token=");
	 		   getRequest.append(User.getUser().getSessionCode()); //Append user token here
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
	 	        
	 	        TranslateServerResponse(response.toString());
	 	        //now make new note
			   
	 		   response = new StringBuilder();
	 		   getRequest = new StringBuilder();
	 		   
	 		   String noteName = User.getUser().getSessionCode() + "_" + User.getUser().getCurrentSessionName();
	 		   
	 		   getRequest.append("http://groupnote.net78.net/newNote.php?token=");
	 		   getRequest.append(User.getUser().getSessionCode()); //Append user token here
	 		   getRequest.append("&note_name=");
	 		   getRequest.append(noteName); //Append session name here
	 		   getRequest.append("&session_id=");
	 		   getRequest.append(User.getUser().getCurrentSession());
	 		   
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
		   protected void onPostExecute(String result)
		   {
			   super.onPostExecute(result);
			   User.getUser().setNoteID(result);
		   }
		   
		   private void TranslateServerResponse(String response)
		   {
		    	
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
		     		if(classes[i].equals(User.getUser().getCurrentSessionName()))
		     		{
		     			User.getUser().setCurrentSession(sectionId[i]);
		     			return;
		     		}
		     	}
		   }
	   }
}
