package com.technotapp.dimon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            lunchMainApp();

        } catch (Exception e) {
            Log.d("ex", e.getMessage());
        }

    }

    private void lunchMainApp() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.technotapp.servicestation");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

}
