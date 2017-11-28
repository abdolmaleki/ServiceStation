package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
            AppMonitor.reportBug(e, mClassName, "initView");
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
                    }
                }
                break;
        }
    }

    //check merchantId and merchantPassword if account is valid return true
    private boolean checkInputValidation(EditText username, EditText password) {
        try {
            if (username.getText().toString().isEmpty()) {
                Toast.makeText(mContext, getString(R.string.SignInActivity_empty_merchantID), Toast.LENGTH_LONG).show();
                return false;
            } else if (password.getText().toString().isEmpty()) {
                Toast.makeText(mContext, getString(R.string.SignInActivity_empty_merchantPassword), Toast.LENGTH_LONG).show();
                return false;
            } else if (username.getText().toString().trim().equals("")) {
                Toast.makeText(mContext, getString(R.string.SignInActivity_invalid_merchant_username), Toast.LENGTH_SHORT).show();
                return false;
            } else if (password.getText().toString().trim().equals("")) {
                Toast.makeText(mContext, getString(R.string.SignInActivity_invalid_merchant_password), Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "checkInputValidation");
            return false;
        }
    }

    private void callGetMenu() {

        try {
            MenuDto menuDto = createMenuDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.TERMINAL_LOGIN).call(mContext, menuDto, AESsecretKey, "در جال بارگیری اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    Gson gson = Helper.getGson();
                    Type listType = new TypeToken<ArrayList<MenuSto>>() {
                    }.getType();
                    ArrayList<MenuSto> menuStos = gson.fromJson(jsonResult, listType);

                    if (menuStos != null) {
                        if (menuStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                            mSession.setLastVersion(menuStos.get(0).messageModel.get(0).ver);

                            if (saveMenu(menuStos) && saveInfo(menuStos)) {
                                startActivity(new Intent(mContext, MainActivity.class));
                            } else {
                                Helper.alert(mContext, menuStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error, Toast.LENGTH_SHORT);
                            }
                        } else {
                            Helper.alert(mContext, menuStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error, Toast.LENGTH_SHORT);
                        }
                    } else {
                        Helper.alert(mContext, getString(R.string.SignInActivity_data_download_error), Constant.AlertType.Error, Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onFail() {
                    Helper.ProgressBar.hideDialog();
                    Helper.alert(mContext, getString(R.string.SignInActivity_serverConnectingError), Constant.AlertType.Error, Toast.LENGTH_SHORT);
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "callGetMenu");
        }
    }

    private MenuDto createMenuDto() {

        MenuDto menuDto = new MenuDto();

//        menuDto.userName = "pirouze";
//        menuDto.password = "4240235464";
        menuDto.userName = edtUsername.getText().toString();
        menuDto.password = edtPassword.getText().toString();
        menuDto.deviceInfo = "My Pos Info";
        menuDto.terminalCode = "R215454D5";
        menuDto.deviceIP = "192.0.0.1";

        return menuDto;
    }

    private boolean saveMenu(List<MenuSto> menuStos) {
        try {

            Db.init();
            List<MenuModel> menuModels = MenuMapper.convertStosToModels(menuStos);
            Db.Menu.insert(menuModels);
            return true;

        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "saveMenu");
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
            session.setGender(menuStos.get(0).dataModel.get(0).info.get(0).gender);
            session.setScore(menuStos.get(0).dataModel.get(0).info.get(0).score);
            session.setHashId(menuStos.get(0).dataModel.get(0).info.get(0).hashId);
            session.setShopName(menuStos.get(0).dataModel.get(0).shop.get(0).title);
            session.setTerminalId(menuStos.get(0).dataModel.get(0).terminalCode);
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "saveInfo");
            return false;
        }
    }
}
