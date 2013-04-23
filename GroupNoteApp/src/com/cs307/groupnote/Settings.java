package com.cs307.groupnote;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        String[] spinnerArray = new String[6];
        spinnerArray[0] = "5";
        spinnerArray[1] = "10";
        spinnerArray[2] = "20";
        spinnerArray[3] = "30";
        spinnerArray[4] = "45";
        spinnerArray[5] = "60";
        
        final Spinner sp = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        sp.setAdapter(spinnerArrayAdapter);
        
        //Set refresh time spinner position from server value.
        User newuser = User.getUser();
        
        int ref = newuser.getRefreshTime();
        
        switch (ref) {
        	case 5: sp.setSelection(0);
        			break;
        	case 10:sp.setSelection(1);
        			break;
        	case 20:sp.setSelection(2);
        			break;
        	case 30:sp.setSelection(3);
        			break;
        	case 45:sp.setSelection(4);
        			break;
        	case 60:sp.setSelection(5);
        			break;
        }
        
        
        //create spinner listener
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	User newuser = User.getUser();
            	newuser.setRefreshTime(Integer.parseInt(sp.getSelectedItem().toString()));
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
        
      //create button listener
        final Button button = (Button) findViewById(R.id.Button03);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	User newuser = User.getUser();
            	newuser.logoutUser();
            	
            	Intent i = new Intent(getBaseContext(), Login.class);
            	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	    	startActivity(i);
            }
        });
    }
    
}
