package com.technotapp.servicestation.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.technotapp.servicestation.connection.restapi.dto.EditProfileDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_img_back)
    LinearLayout back;
    @BindView(R.id.toolbar_tv_title)
    TextView txtTitle;

    private EditText mET_shopName;
    private EditText mET_managerName;
    private EditText mET_tel1;
    private EditText mET_tel2;
    private EditText mET_tel3;
    private EditText mET_mobile;
    private EditText mET_email;
    private EditText mET_address;
    private EditText mET_fax;
    private EditText mET_description;
    private Button mBTN_submit;
    private Session mSession;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        loadData();

        initView();

        fillContents();

    }


    private void loadData() {

    }

    private void initView() {
        ButterKnife.bind(this);
        mET_shopName = findViewById(R.id.activity_edit_profile_et_shopname);
        mET_managerName = findViewById(R.id.activity_edit_profile_et_shop_manager);
        mET_tel1 = findViewById(R.id.activity_edit_profile_et_tel1);
        mET_tel2 = findViewById(R.id.activity_edit_profile_et_tel2);
        mET_tel3 = findViewById(R.id.activity_edit_profile_et_tel3);
        mET_mobile = findViewById(R.id.activity_edit_profile_et_mobile);
        mET_email = findViewById(R.id.activity_edit_profile_et_email);
        mET_address = findViewById(R.id.activity_edit_profile_et_address);
        mET_fax = findViewById(R.id.activity_edit_profile_et_fax);
        mET_description = findViewById(R.id.activity_edit_profile_et_description);
        mBTN_submit = findViewById(R.id.activity_edit_profile_btn_submit);
        mBTN_submit.setOnClickListener(this);
        back.setOnClickListener(this);
        txtTitle.setText("مشخصات فروشگاه");
        mSession = Session.getInstance(this);
    }

    private void fillContents() {
        mET_shopName.setText(mSession.getShopName());
        mET_tel1.setText(mSession.getTelephone());
        mET_mobile.setText(mSession.getMobile());
        mET_email.setText(mSession.getEmail());
        mET_address.setText(mSession.getAddress());
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBTN_submit.getId()) {
            callSubmitShopInfo();
        } else if (id == back.getId()) {
            finish();
        }

    }

    private void callSubmitShopInfo() {
        try {
            EditProfileDto dto = createAddFactorDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.EDIT_SHOP_INFO).call(this, dto, AESsecretKey, "در حال ارسال اطلاعات", new ApiCaller.ApiCallback() {
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
                                Helper.alert(EditProfileActivity.this, "تغییرات با موفقیت ثبت شد", Constant.AlertType.Success);
                                saveInfo();
                            } else {
                                Helper.alert(EditProfileActivity.this, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(EditProfileActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(EditProfileActivity.this, e, "EditProfileActivity", "callSubmitShopInfo");
                        Helper.alert(EditProfileActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(EditProfileActivity.this, message, Constant.AlertType.Error);
                }

            });
        } catch (Exception e) {
            AppMonitor.reportBug(EditProfileActivity.this, e, "EditProfileActivity", "callSubmitShopInfo");
        }
    }

    private EditProfileDto createAddFactorDto() {

        EditProfileDto dto = new EditProfileDto();
        dto.address = mET_address.getText().toString();
        dto.description = mET_description.getText().toString();
        dto.email = mET_email.getText().toString();
        dto.fax = mET_fax.getText().toString();
        dto.managerName = mET_managerName.getText().toString();
        dto.mobile = mET_mobile.getText().toString();
        dto.shopName = mET_shopName.getText().toString();
        dto.tel = mET_tel1.getText().toString();
        dto.tel2 = mET_tel2.getText().toString();
        dto.tel3 = mET_tel3.getText().toString();
        dto.terminalCode = mSession.getTerminalId();
        dto.tokenId = mSession.getTokenId();

        return dto;
    }

    private boolean saveInfo() {
        try {
            Session session = Session.getInstance(this);
            session.setMobileNumber(mET_mobile.getText().toString());
            session.setEmail(mET_mobile.getText().toString());
            session.setAddress(mET_address.getText().toString());
            session.setTel(mET_tel1.getText().toString());
            session.setShopName(mET_shopName.getText().toString());
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(EditProfileActivity.this, e, "EditProfileActivity", "saveInfo");
            return false;
        }
    }
}
