package com.technotapp.servicestation.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.customView.CustomEditText;
import com.technotapp.servicestation.enums.UserRole;

import butterknife.BindView;
import butterknife.ButterKnife;
import lib.kingja.switchbutton.SwitchMultiButton;

public class SettingLoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    private UserRole mUserRole = UserRole.MERCHANT;
    private Button mBTN_confirm;
    private CustomEditText mET_UserName;
    private CustomEditText mET_Password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_login);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("ورود به تنظیمات");
        SwitchMultiButton mSwitchUserRoles = findViewById(R.id.activity_setting_login_switch_roles);
        mBTN_confirm = findViewById(R.id.activity_setting_login_btn_confirm);
        mET_UserName = findViewById(R.id.activity_setting_login_et_username);
        mET_Password = findViewById(R.id.activity_setting_login_et_password);
        mBTN_confirm.setOnClickListener(this);
        back.setOnClickListener(this);

        mSwitchUserRoles.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                switch (position) {
                    case 0:
                        mUserRole = UserRole.MERCHANT;
                        mET_UserName.setVisibility(View.GONE);
                        break;
                    case 1:
                        mUserRole = UserRole.SUPPORTER;
                        mET_UserName.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == back.getId()) {
            finish();
        } else if (id == mBTN_confirm.getId()) {
            if (validation()) {
                Intent intent = new Intent(this, SettingActivity.class);
                intent.putExtra(Constant.Key.USER_ROLE, mUserRole);
                startActivity(intent);
                finish();
            } else {
                Helper.alert(this, "نام کاربری یا رمز عبور را اشتباه است", Constant.AlertType.Error);
            }
        }
    }

    private boolean validation() {
        if (mUserRole == UserRole.SUPPORTER) {
            if (mET_UserName.getText().toString().equals("admin") && (mET_Password.getText().toString().equals("1234"))) {
                return true;

            } else {
                return false;
            }
        } else {
            if (mET_Password.getText().toString().equals("1234")) {
                return true;

            } else {
                return false;
            }
        }
    }
}
