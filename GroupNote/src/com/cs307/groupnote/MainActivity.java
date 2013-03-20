package com.cs307.groupnote;


import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TabHost mTabHost = getTabHost();
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_1").setIndicator("Joined Sessions").setContent(new Intent(this, JoinedSessions.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tab_2").setIndicator("My Notes").setContent(new Intent(this, MyNotes.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tab_3").setIndicator("Find a Session").setContent(new Intent(this, FindSession.class)));
        mTabHost.addTab(mTabHost.newTabSpec("tab_4").setIndicator("Create a Session").setContent(new Intent(this, CreateSession.class)));
        
        mTabHost.setCurrentTab(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
