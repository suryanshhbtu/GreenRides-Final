package com.example.GreenRidersHBTU.Util;
// COMMENTS ADDED
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.GreenRidersHBTU.R;


public class AppInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // back button to close this activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_app_info);
    }
    // back button to close Home
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // close this activity as oppose to navigating up
        return false;
    }
}