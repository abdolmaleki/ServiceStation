package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.MenuDto;
import com.technotapp.servicestation.connection.restapi.sto.MenuSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.database.model.PaymentModel;
import com.technotapp.servicestation.mapper.MenuMapper;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.activity_signin_btnSignin)
    Button btnSignin;
    @BindView(R.id.activity_signin_edtMerchantCode)
    EditText edtUsername;
    @BindView(R.id.activity_signin_edtPassword)
    EditText edtPassword;


    private Context mContext;
    private Session mSession;

    private final String mClassName = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        loadSetting();

        loadData();

        initView();

    }


    private void loadSetting() {

    }

    private void loadData() {
    }

    //initialize views & variables
    private void initView() {
        try {
            ButterKnife.bind(this);
            mContext = SigninActivity.this;
            btnSignin.setOnClickListener(this);
            mSession = Session.getInstance(this);

        } catch (Exception e) {
            AppMonitor.reportBug(this, e, mClassName, "initView");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_signin_btnSignin:
                if (checkInputValidation(edtUsername, edtPassword)) {
                    if (!Helper.IsAppUpToDate()) {
                        callGetMenu();
                    } else {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                break;
        }
    }

    //check merchantId and merchantPassword if account is valid return true
    private boolean checkInputValidation(EditText username, EditText password) {
        try {
            if (username.getText().toString().isEmpty()) {
                Helper.alert(mContext, getString(R.string.SignInActivity_empty_merchantID), Constant.AlertType.Information);
                return false;
            } else if (password.getText().toString().isEmpty()) {
                Helper.alert(mContext, getString(R.string.SignInActivity_empty_merchantPassword), Constant.AlertType.Information);
                return false;
            } else if (!username.getText().toString().trim().equals(username.getText().toString())) {
                Helper.alert(mContext, getString(R.string.SignInActivity_invalid_merchant_username), Constant.AlertType.Error);
                return false;
            } else if (password.getText().toString().trim().equals("")) {
                Helper.alert(mContext, getString(R.string.SignInActivity_invalid_merchant_password), Constant.AlertType.Error);
                return false;
            }
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(this, e, mClassName, "checkInputValidation");
            return false;
        }
    }

    private void callGetMenu() {

        try {
            MenuDto menuDto = createMenuDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.TERMINAL_LOGIN).call(mContext, menuDto, AESsecretKey, "در حال بارگیری اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    try {
                        Gson gson = Helper.getGson();
                        Type listType = new TypeToken<ArrayList<MenuSto>>() {
                        }.getType();
                        ArrayList<MenuSto> menuStos = gson.fromJson(jsonResult, listType);
                        if (menuStos != null) {
                            if (menuStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                if (checkResponseValidation(menuStos.get(0))) {

                                    Helper.progressBar.showDialog(SigninActivity.this, "در حال انجام تنظیمات اولیه");

                                    mSession.setLastVersion(menuStos.get(0).messageModel.get(0).ver);
                                    if (saveMenu(menuStos) && saveInfo(menuStos)) {
                                        startActivity(new Intent(mContext, MainActivity.class));
                                        finish();
                                    } else {
                                        Helper.alert(mContext, "خطا در ذخیره سازی اطلاعات", Constant.AlertType.Error);
                                    }
                                }

                            } else {
                                Helper.alert(mContext, menuStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(mContext, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(SigninActivity.this, e, mClassName, "callGetMenu-OnResponse");
                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.alert(SigninActivity.this, message, Constant.AlertType.Error);
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(SigninActivity.this, e, mClassName, "callGetMenu");
        }
    }

    private boolean checkResponseValidation(MenuSto menuStos) {
        boolean isValid = true;

        if (menuStos.dataModel != null) {
            if (menuStos.dataModel.get(0).shop.get(0) == null) {
                Helper.alert(mContext, "فروشگاهی برای این ترمینال ثبت نشده است.", Constant.AlertType.Error);
                isValid = false;
            }

            if (menuStos.dataModel.get(0).info == null) {
                Helper.alert(mContext, "اطلاعات مربوط به بازرگان دریافت نشد.", Constant.AlertType.Error);
                isValid = false;
            }

            if (menuStos.dataModel.get(0).menu == null) {
                Helper.alert(mContext, "منوی مربوط به ترمینال دریافت نشد", Constant.AlertType.Error);
                isValid = false;
            }


        } else {
            Helper.alert(mContext, "اطلاعات ترمینال دریافت نشد.", Constant.AlertType.Error);
            isValid = false;
        }
        return isValid;
    }

    private MenuDto createMenuDto() {

        MenuDto menuDto = new MenuDto();
        menuDto.userName = edtUsername.getText().toString();
        menuDto.password = edtPassword.getText().toString();
        menuDto.deviceInfo = "My Pos Info";
        menuDto.terminalCode = "08200876";
        return menuDto;
    }

    private boolean saveMenu(List<MenuSto> menuStos) {
        try {

            Db.init(this);
            List<MenuModel> menuModels = MenuMapper.convertStosToModels(menuStos);
            Db.Menu.insert(menuModels);
            List<PaymentModel> paymentModels = MenuMapper.convertPaymentStoToModels(menuStos);
            Db.Payment.insert(paymentModels);
            return true;

        } catch (Exception e) {
            AppMonitor.reportBug(SigninActivity.this, e, mClassName, "saveMenu");
            return false;
        }

    }

    private boolean saveInfo(ArrayList<MenuSto> menuStos) {
        try {

            Session session = Session.getInstance(mContext);
            session.setAppVersion(menuStos.get(0).messageModel.get(0).ver);
            session.setMobileNumber(menuStos.get(0).dataModel.get(0).info.get(0).mobileNumber);
            session.setTokenId(menuStos.get(0).dataModel.get(0).tokenId);
            session.setFirstName(menuStos.get(0).dataModel.get(0).info.get(0).firstName);
            session.setLastName(menuStos.get(0).dataModel.get(0).info.get(0).lastName);
            session.setEmail(menuStos.get(0).dataModel.get(0).info.get(0).email);
            session.setAddress(menuStos.get(0).dataModel.get(0).shop.get(0).address);
            session.setTel(menuStos.get(0).dataModel.get(0).shop.get(0).tel);
            session.setGender(menuStos.get(0).dataModel.get(0).info.get(0).gender);
            session.setScore(menuStos.get(0).dataModel.get(0).info.get(0).score);
            session.setHashId(menuStos.get(0).dataModel.get(0).info.get(0).hashId);
            session.setShopName(menuStos.get(0).dataModel.get(0).shop.get(0).title);
            session.setTerminalId(menuStos.get(0).dataModel.get(0).terminalCode);
            session.setMerchantId(menuStos.get(0).dataModel.get(0).idMerchant);
            session.setMenuCategory(menuStos.get(0).dataModel.get(0).menuCategory);
            session.setBaseUrl(menuStos.get(0).dataModel.get(0).url);
            session.setUserName(edtUsername.getText().toString());
            session.setPinKey(menuStos.get(0).dataModel.get(0).newPinKey);
            session.setHasDiscount(menuStos.get(0).dataModel.get(0).shop.get(0).customersDiscountStatus==1);
            session.setDiscount(menuStos.get(0).dataModel.get(0).shop.get(0).customersDiscountValue);
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(SigninActivity.this, e, mClassName, "saveInfo");
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Helper.progressBar.hideDialog();
    }
}
