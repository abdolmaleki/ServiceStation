package com.technotapp.servicestation.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.fragment.CardServiceFragment;
import com.technotapp.servicestation.fragment.SubMenuFragment;

public class CardServiceActivity extends SubMenuActivity implements IPin {


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
            String backStateName = fragment.getClass().getName();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_card_service_frame, fragment, backStateName);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();

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

    @Override
    public void onPinEntered(String pin) {
        if (mSubmenuContollrer != null) {
            mSubmenuContollrer.onPinEnteredSuccessfully();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof SubMenuFragment) {
            mSubmenuContollrer = (SubMenuFragment) fragment;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mSubmenuContollrer!=null){
            mSubmenuContollrer=null;
        }
    }
}
