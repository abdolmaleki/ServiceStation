package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.fragment.CardServiceFragment;
import com.technotapp.servicestation.fragment.SubMenuFragment;

public class CustomServiceActivity extends SubMenuActivity implements IPin {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_service);

        loadSetting();

        loadData();

        //submitFragment();

    }

    private void submitFragment() {

    }


    private void loadData() {

    }

    private void loadSetting() {

    }

    @Override
    public void onPinEntered(String pin) {
        if (mSubmenuController != null) {
            mSubmenuController.onPinEnteredSuccessfully();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SubMenuFragment) {
            mSubmenuController = (SubMenuFragment) fragment;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSubmenuController != null) {
            mSubmenuController = null;
        }
    }

}
