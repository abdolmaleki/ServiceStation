package com.technotapp.servicestation.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.fragment.CardServiceFragment;

public class CardServiceActivity extends SubMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_service);

        loadSetting();

        loadData();

        bindView();

        submitFragment();

    }

    private void submitFragment() {
        try {
            CardServiceFragment fragment = CardServiceFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_card_service_frame, fragment).commit();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "CardServiceActivity", "submitFragment");
        }
    }

    private void bindView() {

    }


    private void loadData() {

    }


    private void loadSetting() {

    }

}
