package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.Infrastructure.UpdateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.GetVersionDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.enums.UserRole;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.max.slideview.SlideView;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;
    @BindView(R.id.toolbar_img_back)
    LinearLayout back;


    private NeptuneLiteUser neptuneLiteUser = NeptuneLiteUser.getInstance();
    private Session mSession;
    UserRole mUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        loadData();
        initView();
    }

    private void loadData() {
        Intent intent = getIntent();
        if (intent != null) {
            UserRole userRole = (UserRole) intent.getSerializableExtra(Constant.Key.USER_ROLE);
            if (userRole == null) {
                finish();
            } else {
                mUserRole = userRole;
            }
        }
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("تنظیمات");
        back.setOnClickListener(this);
        SlideView slideView = findViewById(R.id.activity_setting_lock_slideView);
        mSession = Session.getInstance(this);


        findViewById(R.id.activity_setting_btn_product).setOnClickListener(this);
        findViewById(R.id.activity_setting_btn_net).setOnClickListener(this);
        findViewById(R.id.activity_setting_btn_update).setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.activity_setting_btn_net) {
            startActivity(new Intent(this, CheckNetworkActivity.class));
        } else if (id == R.id.activity_setting_btn_product) {
            startActivity(new Intent(this, ProductManagementActivity.class));
        } else if (id == R.id.activity_setting_btn_update) {
            checkUpdate();
        } else if (id == back.getId()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void checkUpdate() {
        callGetVersion();
    }

    public void callGetVersion() {
        try {
            GetVersionDto getVersionDto = createVersionDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.GET_VERSION).call(this, getVersionDto, AESsecretKey, "در حال بررسی آخرین تغییرات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<BaseSto>>() {
                        }.getType();
                        List<BaseSto> sto = gson.fromJson(jsonResult, listType);

                        if (sto != null) {
                            if (sto.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                mSession.setLastVersion(sto.get(0).messageModel.get(0).ver);
                                if (!UpdateHelper.checkNeedingUpdate(SettingActivity.this)) {
                                    Helper.alert(SettingActivity.this, "نسخه حاضر آخرین نسخه این نرم افزار می باشد", Constant.AlertType.Information);
                                }
                            } else {
                                Helper.alert(SettingActivity.this, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(SettingActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(SettingActivity.this, e, "SettingActivity", "callGetVersion-onResponse");
                        Helper.alert(SettingActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }
                }

                @Override
                public void onFail() {
                    Helper.alert(SettingActivity.this, getString(R.string.serverConnectingError), Constant.AlertType.Error);

                }

                @Override
                public void onNetworkProblem(String message) {
                    Helper.alert(SettingActivity.this, message, Constant.AlertType.Error);
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(SettingActivity.this, e, "SettingActivity", "callGetVersion");
        }
    }

    private GetVersionDto createVersionDto() {
        GetVersionDto dto = new GetVersionDto();
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        return dto;
    }

}
