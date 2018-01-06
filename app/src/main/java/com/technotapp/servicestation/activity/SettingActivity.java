package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.max.slideview.SlideView;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;
    @BindView(R.id.toolbar_img_back)
    LinearLayout back;

    private NeptuneLiteUser neptuneLiteUser = NeptuneLiteUser.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("تنظیمات");
        back.setOnClickListener(this);
        SlideView slideView = findViewById(R.id.activity_setting_lock_slideView);


        findViewById(R.id.activity_setting_btn_product).setOnClickListener(this);
        findViewById(R.id.activity_setting_btn_net).setOnClickListener(this);

        initSlideView(slideView);
    }

    private void initSlideView(SlideView slideView) {
        if (PaxHelper.isNavigationBarEnable(SettingActivity.this)) {

            slideView.setButtonImage(getResources().getDrawable(R.drawable.ic_locked));
            slideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.RED));
            slideView.setText("غیر فعالسازی نوار پایین");


        } else {
            slideView.setButtonImage(getResources().getDrawable(R.drawable.ic_unlocked));
            slideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.GREEN));
            slideView.setText("فعالسازی نوار پایین");

        }

        slideView.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                if (PaxHelper.isNavigationBarEnable(SettingActivity.this)) {
                    PaxHelper.disableAllNavigationButton(SettingActivity.this);
                    slideView.setButtonImage(getResources().getDrawable(R.drawable.ic_unlocked));
                    slideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.GREEN));
                    slideView.setText("فعالسازی نوار پایین");
                } else {
                    PaxHelper.enableBackNavigationButton(SettingActivity.this);
                    slideView.setButtonImage(getResources().getDrawable(R.drawable.ic_locked));
                    slideView.setButtonBackgroundColor(ColorStateList.valueOf(Color.RED));
                    slideView.setText("غیر فعالسازی نوار پایین");
                }

            }
        });


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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.activity_setting_btn_net) {
            startActivity(new Intent(this, CheckNetworkActivity.class));
        } else if (id == R.id.activity_setting_btn_product) {
            startActivity(new Intent(this, ProductManagementActivity.class));
        } else if (id == back.getId()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
