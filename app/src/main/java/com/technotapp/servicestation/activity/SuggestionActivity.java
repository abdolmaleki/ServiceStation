package com.technotapp.servicestation.activity;

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
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.SuggestionDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.customView.CustomEditText;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SuggestionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    private Button mBTN_submit;
    private Session mSession;

    private CustomEditText mET_title;
    private CustomEditText mET_message;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        txtTitle.setText("نظرات و پیشنهادات");
        back.setOnClickListener(this);
        mBTN_submit = findViewById(R.id.activity_suggestion_btn_submit);
        mET_title = findViewById(R.id.activity_suggestion_et_title);
        mET_message = findViewById(R.id.activity_suggestion_et_message);
        mBTN_submit.setOnClickListener(this);
        mSession = Session.getInstance(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == back.getId()) {
            finish();
        } else if (id == mBTN_submit.getId()) {
            callSubmitSuggestion();
        }
    }

    public void callSubmitSuggestion() {
        try {
            SuggestionDto dto = createDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.SUBMIT_SUGGESTION).call(this, dto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
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
                                Helper.alert(SuggestionActivity.this, "نظر شما با موفقیت ارسال شد", Constant.AlertType.Success);
                            } else {
                                Helper.alert(SuggestionActivity.this, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(SuggestionActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(SuggestionActivity.this, e, "SuggestionActivity", "callSubmitSuggestion-onResponse");
                        Helper.alert(SuggestionActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(SuggestionActivity.this, message, Constant.AlertType.Error);
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(SuggestionActivity.this, e, "SuggestionActivity", "callSubmitSuggestion");
        }
    }

    private SuggestionDto createDto() {
        SuggestionDto dto = new SuggestionDto();
        dto.terminalCode = mSession.getTerminalId();
        dto.tokenId = mSession.getTokenId();
        dto.userDeviceInfo = "My Device Info.";
        dto.title = mET_title.getText().toString();
        dto.description = mET_message.getText().toString();
        dto.deviceIp = "";
        return dto;
    }
}
