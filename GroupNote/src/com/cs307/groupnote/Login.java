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
            	Intent i = new Intent(getBaseContext(), MainActivity.class);
            	startActivity(i);
            }
        });
    }

    
}
