package com.cs307.groupnote;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        //create button
        final Button button = (Button) findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	if (!User.alreadyLoggedIn()) { //User is not currently logged in, this prevents unnecessary server calls.
            		//Do server connection call here...
            		//Set user information
            		
            		User newuser = User.getUser();
            		newuser.setSessionCode("8725630bbd78be6bdcd29e0fb04ef0cb");
            		newuser.setUsername("justini");
            	}
            	
            		//Must stop this from happening if the user fails authentication.
            		Intent i = new Intent(getBaseContext(), MainActivity.class);
            		startActivity(i);
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

    
}
