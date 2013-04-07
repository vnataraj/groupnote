package com.cs307.groupnote;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class Settings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        
      //create button
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
