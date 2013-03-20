package com.cs307.groupnote;


import java.util.ArrayList;
import java.util.Arrays;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class FindSession extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_session);
        
        
     // Find the ListView resource.   
        ListView mainListView = (ListView) findViewById( R.id.listView1 );  
      
        // Create and populate a List of planet names.  
        String[] classes = new String[] { "CS 307", "ENGR 210", "CS348", "MA 261"};    
        ArrayList<String> classList = new ArrayList<String>();  
        classList.addAll( Arrays.asList(classes) );
        
     // Create ArrayAdapter using the planet list.  
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
    
}
