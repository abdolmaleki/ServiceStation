package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.activity_signin_btnSignin)
    Button btnSignin;
    @BindView(R.id.activity_signin_edtMerchantCode)
    EditText edtUsername;
    @BindView(R.id.activity_signin_edtPassword)
    EditText edtPassword;

    private String fakeUsername = "0000";
    private String fakePassword = "0000";
    private Context mContext;

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
                Toast.makeText(mContext, R.string.SignInActivity_empty_merchantID, Toast.LENGTH_LONG).show();
                return false;
            } else if (password.getText().toString().isEmpty()) {
                Toast.makeText(mContext, R.string.SignInActivity_empty_merchantPassword, Toast.LENGTH_LONG).show();
                return false;
            } else if (!username.getText().toString().trim().equals(fakeUsername)) {
                Toast.makeText(mContext, getString(R.string.SignInActivity_invalid_merchant_username), Toast.LENGTH_SHORT).show();
                return false;
            } else if (!password.getText().toString().trim().equals(fakePassword)) {
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
            Helper.ProgressBar.showDialog(mContext, getString(R.string.SignInActivity_loading));

            new ApiCaller(Constant.Api.Type.TERMINAL_LOGIN).call(menuDto, AESsecretKey, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Helper.ProgressBar.hideDialog();
                    String EncryptedResponse = response.body();
                    if (EncryptedResponse == null || EncryptedResponse.isEmpty()) {
                        Helper.alert(mContext, getString(R.string.SignInActivity_data_download_error), Constant.AlertType.Error, Toast.LENGTH_SHORT);

                    } else {
                        Gson gson = new GsonBuilder()
                                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                                .create();

                        String AESsecretKeyString = Base64.encodeToString(AESsecretKey.getEncoded(), Base64.DEFAULT);
                        String decryptedResponseString = Encryptor.decriptAES(AESsecretKeyString, EncryptedResponse);
                        String formattedJsonString = gson.fromJson(decryptedResponseString, String.class);
                        Type listType = new TypeToken<ArrayList<MenuSto>>() {
                        }.getType();
                        ArrayList<MenuSto> menuStos = gson.fromJson(formattedJsonString, listType);

                        if (menuStos != null) {
                            if (menuStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                                if (saveMenu(menuStos)) {
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
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
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

        menuDto.userName = "pirouze";
        menuDto.password = "4240235464";
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

}
