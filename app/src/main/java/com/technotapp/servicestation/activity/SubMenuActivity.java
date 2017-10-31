package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.fragment.KeypadFragment;
import com.technotapp.servicestation.fragment.SubMenuFragment;

public class SubMenuActivity extends AppCompatActivity implements IToolBar {

    protected SubMenuFragment mSubmenuContollrer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void back() {

        try {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                String stackFragment = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                if (stackFragment.equals(KeypadFragment.class.getName())) {
                    fragmentManager.popBackStack();
                }
                fragmentManager.popBackStack();


                fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        //todo change this if
                         if(fragmentManager.getBackStackEntryCount() == 0){

                            finish();
                        }
                        else if (fragmentManager.getBackStackEntryCount() == 1) {
                            String currentFragment = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
                            if (currentFragment != null) {
                                Fragment fragment = fragmentManager.findFragmentByTag(currentFragment);
                                mSubmenuContollrer = (SubMenuFragment) fragment;
                            }
                        }

                    }
                });


            } else {
                finish();
            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "SubMenuActivity", "back");
        }

    }

    @Override
    public void setTitle(String title) {

        LinearLayout ll = (LinearLayout) findViewById(R.id.toolbar_main);
        TextView tv_title = (TextView) ll.findViewById(R.id.toolbar_tv_title);
        tv_title.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout ll = (LinearLayout) findViewById(R.id.toolbar_main);
        ImageButton img_back = (ImageButton) ll.findViewById(R.id.toolbar_img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }
}
