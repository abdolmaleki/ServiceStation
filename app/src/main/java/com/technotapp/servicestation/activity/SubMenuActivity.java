package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.fragment.SubMenuFragment;

public abstract class SubMenuActivity extends AppCompatActivity implements IToolBar {

    public SubMenuFragment mSubmenuController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void back() {
        //Todo change back function
        try {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 1) {

                String oneLastFragment = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 2).getName();

                fragmentManager.popBackStack();

                if (oneLastFragment != null) {
                    Fragment fragment = fragmentManager.findFragmentByTag(oneLastFragment);
                    mSubmenuController = (SubMenuFragment) fragment;
                }

            } else {
                finish();
            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "SubMenuActivity", "back");
        }

    }

    @Override
    public void setTitle(String title) {
        try {
            LinearLayout ll = (LinearLayout) findViewById(R.id.toolbar_main);
            TextView tv_title = (TextView) ll.findViewById(R.id.toolbar_tv_title);
            tv_title.setText(title);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "SubMenuActivity", "setTitle");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            LinearLayout ll = (LinearLayout) findViewById(R.id.toolbar_main);
            ImageButton img_back = (ImageButton) ll.findViewById(R.id.toolbar_img_back);
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, "SubMenuActivity", "onResume");
        }
    }
}
