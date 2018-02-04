package com.technotapp.servicestation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.setting.AppSetting;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    private SeekBar mSeek_brightness;
    private Switch mSwitch_turning;
    private Switch mSwitch_seller_print;
    private Switch mSwitch_customer_print;

    AppSetting appSetting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("پیکربندی");
        back.setOnClickListener(this);

        mSeek_brightness = findViewById(R.id.activity_config_seek_brightness);
        mSeek_brightness.setOnSeekBarChangeListener(this);
        mSeek_brightness.setProgress(PaxHelper.getDeviceBrighness(this));

        mSwitch_turning = findViewById(R.id.activity_config_switch_turning);
        mSwitch_turning.setOnCheckedChangeListener(this);

        mSwitch_seller_print = findViewById(R.id.activity_config_switch_seller_printer);
        mSwitch_seller_print.setOnCheckedChangeListener(this);

        mSwitch_customer_print = findViewById(R.id.activity_config_switch_customer_printer);
        mSwitch_customer_print.setOnCheckedChangeListener(this);

        appSetting = AppSetting.getInstance(this);

        mSwitch_seller_print.setChecked(appSetting.isSellerPrintEnabled());
        mSwitch_customer_print.setChecked(appSetting.isCustomerPrintEnabled());
        mSwitch_turning.setChecked(appSetting.isTurningEnabled());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == back.getId()) {
            finish();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (seekBar.getId() == mSeek_brightness.getId()) {
            PaxHelper.setBrightness(this, i);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        if (compoundButton.getId() == mSwitch_turning.getId()) {
            if (checked) {
                appSetting.setIsTurningEnable(true);

            } else {
                appSetting.setIsTurningEnable(false);
            }
        } else if (compoundButton.getId() == mSwitch_customer_print.getId()) {
            if (checked) {
                appSetting.setCustomerPrintEnabled(true);

            } else {
                appSetting.setCustomerPrintEnabled(false);
            }
        } else if (compoundButton.getId() == mSwitch_seller_print.getId()) {
            if (checked) {
                appSetting.setSellerPrintEnabled(true);

            } else {
                appSetting.setSellerPrintEnabled(false);
            }
        }
    }
}
