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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class EditNote extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);
        
        //add button action commands
        final Button refreshButton = (Button) findViewById(R.id.button2);
        refreshButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				TextView noteView = (TextView)findViewById(R.id.notetext);
				String NoteString = noteView.getText().toString();
				//new SaveNote().execute(serializeObject(NoteString));
				new SaveNote().execute(NoteString);
				
			}
		});
        
        new GetNote().execute("");


        
    }
    
    /*To Do Here
     * Given Session ID and user ID download note
     * save note when user leaves page
     * Update sessionname textfield
     * save note when refresh button is pressed
     * navigate to NotesOverview Page when button is pressed (pass in session id and maintain user token)
     * Know when page is being used for other users note, disable text box, the refresh button only downloads note, not saves changes. 
     */

    private void populateTextBox(String note)
    {
    	TextView noteView = (TextView)findViewById(R.id.notetext);
    	noteView.setText(note);
        TextView sessionNameLabel = (TextView) findViewById(R.id.sessionnametext);
        sessionNameLabel.setText("Session: " +User.getUser().getCurrentSessionName());
        //sessionNameLabel.setText("NoteID: " +User.getUser().getNoteID());
    	
    }
   
    private class SaveNote extends AsyncTask<String,Void,String>
    {
    	@Override
    	protected String doInBackground(String...params)
    	{
    		StringBuilder response = new StringBuilder();
    		StringBuilder getRequest = new StringBuilder();
    		
    		getRequest.append("http://groupnote.net78.net/editNote.php?note_id=");
    		getRequest.append(User.getUser().getNoteID());
    		getRequest.append("&content=");
    		String input= params.toString();
    		getRequest.append(input); //the note text
    		getRequest.append("&token=");
    		getRequest.append(User.getUser().getSessionCode());
    		
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
    				while((line =r.readLine()) != null)
    				{
    					response.append(line);
    				}
    			}
    		}
    		catch(Exception e)
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
    		if(result.equals("false"))
    		{
    			Toast toast = Toast.makeText(getApplicationContext(), "Error syncing to server! Please try again.",Toast.LENGTH_SHORT);
    			toast.show();
    		}
    	}
    }
    public byte[] serializeObject(Object o)
    {
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	try
    	{
    		ObjectOutput out = new ObjectOutputStream(bos);
    		out.writeObject(o);
    		out.close();
    		byte[] buf = bos.toByteArray();
    		return buf;
    	}
    	catch(IOException ioe)
    	{
    		Log.e("serializeObject", "error",ioe);
    	}
    	return null;
    }
    public String deserializeObject(byte[] b)
    {
    	try
    	{
    		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
    		Object object = in.readObject();
    		in.close();
    		return object.toString();
    	}
    	catch(ClassNotFoundException cnfe)
    	{
    		Log.e("deserializeObject","class not found error",cnfe);
    		return null;
    	}
    	catch(IOException ioe)
    	{
    		Log.e("deserializeObject","io error",ioe);
    		return null;
    	}
    }

	public String getNoteId(String response)
	{
		String[] notes;
		String[] users;
		int noteCount = 0;
		
		//break string into arrays of noteIDs and userIDs
		for(int i = 0; i < response.length(); i++)
		{
			if(response.charAt(i) == ',') noteCount++;;
		}
		notes = new String[noteCount];
		users = new String[noteCount];
		int counter = 0;
		for(int i = 0; i < noteCount;i++)
		{
			//notes[i] = "";
			//users[i] = "";
			
			char tempchar;
			StringBuilder tempstring = new StringBuilder();
			while((tempchar = response.charAt(counter++)) != ','){ //??OUT OF BOUNDS ERROR, HOW???
				tempstring.append(Character.toString(tempchar));
			}
			notes[i] = tempstring.toString();
			
			tempstring = new StringBuilder();
			while(counter < (response.length()) &&(tempchar=response.charAt(counter++)) != ' ' ){
				tempstring.append(Character.toString(tempchar));
			}
			users[i] = tempstring.toString();
		}
		for(int i = 0; i < noteCount; i++)
		{
			if(users[i].equals(User.getUser().getUsername()))
				return notes[i];
		}
		return "";
	}
    //private async class for retrieving the note from the server
    private class GetNote extends AsyncTask<String, Void, String>
    {
    	@Override
    	protected String doInBackground(String...params)
    	{
    		//call getNotesForSession to find the note_id, 
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
    					response.append(line);
    				}
    				
    			}
    			
    		}
    		catch (Exception e)
    		{
    			runOnUiThread(new Runnable() {
    				public void run() {
    					Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to server! Please try again.",Toast.LENGTH_SHORT);
    					toast.show();
    				}
    			});
    		}
    		//return response.toString();
    		//new GetNoteID().execute("");
    		String noteID = User.getUser().setNoteID(getNoteId(response.toString()));
    		
    		//call getNote to get the contents of the note. 
    		 response = new StringBuilder();
    		 getRequest = new StringBuilder();
    		
    		getRequest.append("http://groupnote.net78.net/getNote.php?token=");
    		getRequest.append(User.getUser().getSessionCode());
    		getRequest.append("&note_id=");
    		//getRequest.append(User.getUser().getNoteID());
    		getRequest.append(noteID);
    		
    		try
    		{
    			HttpClient client = new DefaultHttpClient();
    			URI getURI = new URI(getRequest.toString());
    			HttpGet get = new HttpGet();
    			get.setURI(getURI);
    			HttpResponse responseGet = client.execute(get);
    			HttpEntity resEntityGet = responseGet.getEntity();
    			if(resEntityGet != null)
    			{
    				BufferedReader r = new BufferedReader(new InputStreamReader(resEntityGet.getContent()));
    				String line;
    				while((line = r.readLine()) != null)
    				{
    					response.append(line);
    				}
    				
    			}
    			
    		}
    		catch (Exception e)
    		{
    			runOnUiThread(new Runnable() {
    				public void run() {
    					Toast toast = Toast.makeText(getApplicationContext(), "Error connecting to server! PLease try again.",Toast.LENGTH_SHORT);
    					toast.show();
    				}
    			});
    		}
    		
    		byte[] data = Base64.decode(response.toString(), Base64.DEFAULT);
    		//Convert response to byte[] or change the way the function works. 
    		return  deserializeObject(data);
    	}
    	

    	
    	@Override
    	protected void onPostExecute(String result)
    	{
    		super.onPostExecute(result);
    		populateTextBox(result);
    	}
    }
}
