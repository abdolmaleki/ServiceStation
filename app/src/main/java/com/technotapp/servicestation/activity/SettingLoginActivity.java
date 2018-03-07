package com.technotapp.servicestation.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.UpdateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.GetVersionDto;
import com.technotapp.servicestation.connection.restapi.dto.SettingLoginDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.connection.restapi.sto.SettingLoginSto;
import com.technotapp.servicestation.customView.CustomEditText;
import com.technotapp.servicestation.enums.UserRole;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import lib.kingja.switchbutton.SwitchMultiButton;

public class

SettingLoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    private UserRole mUserRole = UserRole.MERCHANT;
    private Button mBTN_confirm;
    private CustomEditText mET_UserName;
    private CustomEditText mET_Password;
    private Session mSession;

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
        mSession = Session.getInstance(this);
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
                if (mUserRole == UserRole.MERCHANT) {
                    Intent intent = new Intent(this, SettingActivity.class);
                    intent.putExtra(Constant.Key.USER_ROLE, mUserRole);
                    startActivity(intent);
                    finish();
                } else {
                    callLoginSetting();
                }
            } else {
                Helper.alert(this, "نام کاربری یا رمز عبور را اشتباه است", Constant.AlertType.Error);
            }
        }
    }

    private boolean validation() {
        if (mUserRole == UserRole.MERCHANT) {
            if (mET_Password.getText().toString().equals("1234")) {
                return true;

            } else {
                return false;
            }
        } else {
            return true;
        }

    }

    public void callLoginSetting() {
        try {
            SettingLoginDto settingLoginDto = createDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.LOGIN_SETTING).call(this, settingLoginDto, AESsecretKey, "در حال احراز هویت", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<SettingLoginSto>>() {
                        }.getType();
                        List<SettingLoginSto> sto = gson.fromJson(jsonResult, listType);

                        if (sto != null) {
                            if (sto.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(sto.get(0).messageModel.get(0).ver);
                                UpdateHelper.checkNeedingUpdate(SettingLoginActivity.this);
                                Intent intent = new Intent(SettingLoginActivity.this, SettingActivity.class);
                                intent.putExtra(Constant.Key.USER_ROLE, mUserRole);
                                intent.putExtra(Constant.Key.SUPPORT_TOKEN_ID, sto.get(0).dataModel.get(0).data.get(0).tokenId);
                                intent.putExtra(Constant.Key.SUPPORT_TOKEN_ID_EXPIRE_TIME, sto.get(0).dataModel.get(0).data.get(0).tokenExpireDate);
                                intent.putExtra(Constant.Key.SUPPORT_IMAGE, sto.get(0).dataModel.get(0).data.get(0).imageName);
                                intent.putExtra(Constant.Key.SUPPORT_IMAGE_URL, sto.get(0).dataModel.get(0).data.get(0).url);
                                intent.putExtra(Constant.Key.SUPPORTER_NAME, sto.get(0).dataModel.get(0).data.get(0).name + " " + sto.get(0).dataModel.get(0).data.get(0).lastName);
                                startActivity(intent);
                                finish();
                            } else {
                                Helper.alert(SettingLoginActivity.this, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(SettingLoginActivity.this, "خطا در دریافت اطلاعات", Constant.AlertType.Error);

                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(SettingLoginActivity.this, e, "SettingLoginActivity", "callLoginSetting-onResponse");

                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(SettingLoginActivity.this, message, Constant.AlertType.Error);
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(SettingLoginActivity.this, e, "SettingLoginActivity", "callGetVersion");
        }
    }

    private SettingLoginDto createDto() {
        SettingLoginDto dto = new SettingLoginDto();
        dto.deviceIP = "";
        dto.password = mET_Password.getText().toString();
        dto.userName = mET_UserName.getText().toString();
        dto.terminalCode = mSession.getTerminalId();
        dto.tokenId = mSession.getTokenId();

        return dto;
    }
}
