package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.UpdateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.TerminalInfoDto;
import com.technotapp.servicestation.connection.restapi.sto.MenuSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.mapper.MenuMapper;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public class UpdatingActivity extends AppCompatActivity {

    private Context mContext;
    private final String mClassName = getClass().getSimpleName();
    private Session mSession;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating);

        initView();
    }

    private void initView() {
        mContext = this;
        mSession = Session.getInstance(this);
    }

    public void callGetMenu() {
        try {
            TerminalInfoDto terminalInfoDto = createTerminalInfoDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            Helper.ProgressBar.showDialog(mContext, getString(R.string.SignInActivity_loading));

            new ApiCaller(Constant.Api.Type.TERMINAL_LOGIN).call(mContext, terminalInfoDto, AESsecretKey, new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    Gson gson = Helper.getGson();
                    Type listType = new TypeToken<ArrayList<MenuSto>>() {
                    }.getType();
                    ArrayList<MenuSto> menuStos = gson.fromJson(jsonResult, listType);

                    if (menuStos != null) {
                        if (menuStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                            int appVersion = menuStos.get(0).messageModel.get(0).ver;
                            UpdateHelper.setLastVersion(appVersion);
                            mSession.setAppVersion(appVersion);

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

    private TerminalInfoDto createTerminalInfoDto() {

        TerminalInfoDto terminalInfoDto = new TerminalInfoDto();

        terminalInfoDto.terminalCode = "R215454D5";
        terminalInfoDto.deviceIP = "192.0.0.1";
        terminalInfoDto.tokenId = mSession.getTokenId();


        return terminalInfoDto;
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
            session.setShopName(menuStos.get(0).dataModel.get(0).shop.get(0).title);
            session.setTerminalId(menuStos.get(0).dataModel.get(0).terminalCode);
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(e, mClassName, "saveInfo");
            return false;
        }

    }
}
