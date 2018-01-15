package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.NetworkHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.TerminalInfoDto;
import com.technotapp.servicestation.connection.restapi.sto.MenuSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.mapper.MenuMapper;
import com.technotapp.servicestation.setting.Session;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.SecretKey;

public class UpdatingActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private final String mClassName = getClass().getSimpleName();
    private Session mSession;
    private ColorfulRingProgressView mProgressView;
    private int progressStatus = 0;
    private Button mBTN_retry;
    private Button mBTN_exit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updating);

        initView();
        doUpdate();
    }

    private void doUpdate() {

        goToUpdatingState();

        NetworkHelper.isConnectingToInternet(this, new NetworkHelper.CheckNetworkStateListener() {
            @Override
            public void onNetworkChecked(boolean isSuccess, String message) {
                if (isSuccess) {
                    startUpdate();
                } else {
                    Helper.alert(UpdatingActivity.this, "به دلیل عدم ارتباط با اینترنت بروزرسانی ممکن نمی باشد", Constant.AlertType.Error);
                    goToFailedState();
                }
            }
        });
    }

    private void goToUpdatingState() {
        mBTN_retry.setVisibility(View.INVISIBLE);
        mBTN_exit.setVisibility(View.INVISIBLE);
        mProgressView.setVisibility(View.VISIBLE);
    }

    private void goToFailedState() {
        mBTN_retry.setVisibility(View.VISIBLE);
        mBTN_exit.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(View.INVISIBLE);
    }

    private void startUpdate() {


        try {
            final Handler handler = new Handler(Looper.getMainLooper());

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Looper.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressView.setVisibility(View.VISIBLE);

                        }
                    });
                    while (progressStatus < 90) {
                        progressStatus += 1;
                        handler.post(new Runnable() {
                            public void run() {
                                mProgressView.setPercent(progressStatus + 0.f);
                            }
                        });
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            AppMonitor.reportBug(UpdatingActivity.this, e, mClassName, "startUpdate");
                        }
                    }
                    callGetTerminalInfo();
                }
            }, 2000);


        } catch (Exception e) {
            AppMonitor.reportBug(UpdatingActivity.this, e, mClassName, "startUpdate");
        }
    }

    private void initView() {
        mContext = this;
        mProgressView = findViewById(R.id.activity_updating_progress);
        mSession = Session.getInstance(this);
        mBTN_retry = findViewById(R.id.activity_updating_btn_retry);
        mBTN_retry.setOnClickListener(this);
        mBTN_exit = findViewById(R.id.activity_updating_btn_exit);
        mBTN_exit.setOnClickListener(this);

    }

    public void callGetTerminalInfo() {
        try {
            TerminalInfoDto terminalInfoDto = createTerminalInfoDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.TERMINAL_INFO).call(mContext, terminalInfoDto, AESsecretKey, "در جال بارگیری اطلاعات", new ApiCaller.ApiCallback() {
                @Override
                public void onResponse(int responseCode, String jsonResult) {
                    Gson gson = Helper.getGson();
                    Type listType = new TypeToken<ArrayList<MenuSto>>() {
                    }.getType();
                    ArrayList<MenuSto> menuStos = gson.fromJson(jsonResult, listType);

                    if (menuStos != null) {
                        if (menuStos.get(0).messageModel.get(0).errorCode == Constant.Api.ErrorCode.Successfull) {
                            if (saveMenu(menuStos) && saveInfo(menuStos)) {
                                int appVersion = menuStos.get(0).messageModel.get(0).ver;
                                mSession.setLastVersion(appVersion);
                                mSession.setAppVersion(appVersion);
                                mProgressView.setPercent(100.0f);
                                Helper.alert(UpdatingActivity.this, "بروزرسانی با موفقیت انجام شد", Constant.AlertType.Success);
                                startActivity(new Intent(mContext, MainActivity.class));
                            } else {
                                Helper.alert(mContext, menuStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                                goToFailedState();
                            }
                        } else {
                            Helper.alert(mContext, menuStos.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            goToFailedState();
                        }
                    } else {
                        Helper.alert(mContext, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        goToFailedState();
                    }
                }

                @Override
                public void onFail(String message) {
                    Helper.progressBar.hideDialog();
                    Helper.alert(UpdatingActivity.this, message, Constant.AlertType.Error);
                    goToFailedState();
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(UpdatingActivity.this, e, mClassName, "callGetTerminalInfo");
            goToFailedState();
        }

    }

    private TerminalInfoDto createTerminalInfoDto() {

        TerminalInfoDto terminalInfoDto = new TerminalInfoDto();
        terminalInfoDto.terminalCode = mSession.getTerminalId();
        terminalInfoDto.tokenId = mSession.getTokenId();


        return terminalInfoDto;
    }

    private boolean saveMenu(List<MenuSto> menuStos) {
        try {

            Db.init(this);
            List<MenuModel> menuModels = MenuMapper.convertStosToModels(menuStos);
            Db.Menu.insert(menuModels);
            return true;

        } catch (Exception e) {
            AppMonitor.reportBug(UpdatingActivity.this, e, mClassName, "saveMenu");
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
            session.setTel(menuStos.get(0).dataModel.get(0).shop.get(0).tel);
            session.setHashId(menuStos.get(0).dataModel.get(0).info.get(0).hashId);
            session.setTerminalId(menuStos.get(0).dataModel.get(0).terminalCode);
            session.setMerchantId(menuStos.get(0).dataModel.get(0).idSeller);
            session.setMenuCategory(menuStos.get(0).dataModel.get(0).menuCategory);
            return true;
        } catch (Exception e) {
            AppMonitor.reportBug(UpdatingActivity.this, e, mClassName, "saveInfo");
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBTN_retry.getId()) {
            doUpdate();
        } else if (id == mBTN_exit.getId()) {
            finish();
        }

    }
}
