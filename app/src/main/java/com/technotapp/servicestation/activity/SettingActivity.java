package com.technotapp.servicestation.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.R;

public class SettingActivity extends AppCompatActivity {
    private NeptuneLiteUser neptuneLiteUser = NeptuneLiteUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        configDevice();
        finish();
    }

    private void configDevice() {
        try {
            neptuneLiteUser.getDal(getApplicationContext()).getSys().enableNavigationBar(true);
            neptuneLiteUser.getDal(getApplicationContext()).getSys().showNavigationBar(true);
            neptuneLiteUser.getDal(getApplicationContext()).getSys().showStatusBar(true);
            neptuneLiteUser.getDal(getApplicationContext()).getSys().enableStatusBar(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
