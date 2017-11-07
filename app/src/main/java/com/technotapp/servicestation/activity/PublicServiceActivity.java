package com.technotapp.servicestation.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.fragment.CardServiceFragment;
import com.technotapp.servicestation.fragment.ChargeFragment;
import com.technotapp.servicestation.fragment.SubMenuFragment;

public class PublicServiceActivity extends SubMenuActivity implements IPin {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_service);

        loadSetting();

        loadData();

        submitFragment();

    }

    private void submitFragment() {
        try {
            Fragment fragment = getFragment(getIntent().getStringExtra(Constant.Key.CURRENT_FRAGMENT));
            String backStateName = fragment.getClass().getName();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_public_service_frame, fragment, backStateName);
            fragmentTransaction.addToBackStack(backStateName);
            fragmentTransaction.commit();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "PublicServiceActivity", "submitFragment");
        }
    }

    private Fragment getFragment(String fragmnt) {
        switch (fragmnt) {
            case Constant.MainItem.CHARGE:
                return ChargeFragment.newInstance();

            case Constant.MainItem.CARDSERVICE:
                return CardServiceFragment.newInstance();



        }
        return null;
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
