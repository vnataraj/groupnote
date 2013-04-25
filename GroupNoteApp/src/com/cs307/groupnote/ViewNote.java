package com.cs307.groupnote;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
public class ViewNote extends Activity {

	private int[] noteIds;
	private String[] members;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);
        new GetMembers().execute("");
    }
    public void populateList()
    {
    	GridView mainGridView = (GridView) findViewById(R.id.gridView1);
    	ArrayList<String> memberList = new ArrayList<String>();
    	memberList.addAll(Arrays.asList(members));
    	
    	ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,R.layout.listrow,memberList);
    	mainGridView.setAdapter(listAdapter);
    	//Add Click Listener
    	mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    		public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
    			// Perform action on click
    			//String[] splits = adapter.getItemAtPosition(position).toString().split(", ");
    			int pos = position;
    			User u = User.getUser();
    			int n = noteIds[position];
    			String name = members[position];
    			String test = adapter.getItemAtPosition(position).toString();
    			User.getUser().setNoteID(noteIds[position]+"");
    			//if(User.getUser().getUsername().compareTo(adapter.getItemAtPosition(position).toString())==0)
    			if(User.getUser().getUsername().compareTo(members[position]) ==0)
    			{
    				User.getUser().setOtherNote(false);
    			}
    			else
    			{
    				//User.getUser().setOtherName(adapter.getItemAtPosition(position).toString());
    				User.getUser().setOtherName(members[position]);
    				User.getUser().setOtherNote(true);
    			}
    			Intent i = new Intent(getBaseContext(),EditNote.class);
    			startActivity(i);
    			//User newuser = User.getUser();
	 	    	//newuser.setCurrentSession(Integer.parseInt(splits[0]));
	 	    	//newuser.setCurrentSessionName(splits[1]);
    			//new NewNote().execute(""); //creates a new note for the user in this session
    			//Intent i = new Intent(getBaseContext(), EditNote.class);
    			//startActivity(i);
    		}
    	}); 
    }
    /*To Do here
     * given session id and user id download all users with notes. 
     * when a users name is clicked open that page in EditNote but send message that it shouldnt be editable.  
     * 
     */
    public void TranslateServerResponse(String response)
    {
    	int memberCount = 0;
    	
    	for(int i = 0; i < response.length(); i++)
    	{
    		if(response.charAt(i) == ';')
    			memberCount++;
    	}
    	members = new String[memberCount];
    	noteIds = new int[memberCount];
    	int counter = 0;
    	for(int i = 0; i < memberCount; i++)
    	{
    		members[i] = "";
    		noteIds[i] = -1;
    		
    		char tempChar;
    		StringBuilder tempString = new StringBuilder();
    		while((tempChar = response.charAt(counter++)) != ',')
    		{
    			tempString.append(Character.toString(tempChar));
    		}
    		noteIds[i] = Integer.parseInt(tempString.toString());
    		tempString = new StringBuilder();
    		while((tempChar=response.charAt(counter++)) != ';')
    		{
    			tempString.append(Character.toString(tempChar));
    		}
    		members[i] = tempString.toString();	
    	}
    }
    
    private class GetMembers extends AsyncTask<String,Void,String>
    {
    	@Override
    	protected String doInBackground(String...params)
    	{
    		StringBuilder response = new StringBuilder();
    		StringBuilder getRequest = new StringBuilder();
    		
    		getRequest.append("http://groupnote.net78.net/getSessionNotes.php?token=");
    		getRequest.append(User.getUser().getSessionCode()); 
    		getRequest.append("&session_id=");
    		getRequest.append(User.getUser().getCurrentSession());
    		
    		try
    		{
    			HttpClient client = new DefaultHttpClient();
    			URI getURL = new URI(getRequest.toString());
    			HttpGet get = new HttpGet();
    			get.setURI(getURL);
    			HttpResponse responseGet = client.execute(get);
    			HttpEntity resEntityGet = responseGet.getEntity();
    			if(resEntityGet != null)
    			{
    				BufferedReader r = new BufferedReader(new InputStreamReader(resEntityGet.getContent()));
    				String line;
    				while((line = r.readLine()) != null)
    				{
    					if(line.equals(""))
    						response.append(";");
    					else
    						response.append(line);
    					

    				}
    			}
    			
    		}
    		catch(Exception ex)
    		{
       			runOnUiThread(new Runnable() {
    				public void run() {
    					Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to server! Please try again.",Toast.LENGTH_SHORT);
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
    		TranslateServerResponse(result);
    		populateList();
    	}
    }
}
