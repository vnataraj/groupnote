package com.cs307.groupnote;


import android.os.Bundle;
import android.app.Activity;

public class NotesOverview extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_overview);
    }
    /*To Do here
     * given session id and user id download all users with notes. 
     * when a users name is clicked open that page in EditNote but send message that it shouldnt be editable.  
     * 
     */
}
