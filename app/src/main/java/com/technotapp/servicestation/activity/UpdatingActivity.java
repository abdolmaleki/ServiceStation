package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pitt.library.fresh.FreshDownloadView;
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
import com.technotapp.servicestation.database.model.PaymentModel;
import com.technotapp.servicestation.mapper.MenuMapper;
import com.technotapp.servicestation.setting.Session;

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
    private FreshDownloadView mProgressView;
    private int progressStatus = 0;
    private Button mBTN_retry;
    private Button mBTN_exit;
    private TextView mTV_message;
    private Button mBTN_setting;


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
                    while (progressStatus < 99) {
                        progressStatus += 1;
                        handler.post(new Runnable() {
                            public void run() {
                                mProgressView.upDateProgress(progressStatus);
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
            }, 0);


        } catch (Exception e) {
            AppMonitor.reportBug(UpdatingActivity.this, e, mClassName, "startUpdate");
        }
    }

    private void initView() {
        mContext = this;
        mProgressView = findViewById(R.id.activity_updating_progress);
        mSession = Session.getInstance(this);
        mBTN_retry = findViewById(R.id.activity_updating_btn_retry);
        mTV_message = findViewById(R.id.activity_updating_tv_message);
        mBTN_retry.setOnClickListener(this);
        mBTN_exit = findViewById(R.id.activity_updating_btn_cancel);
        mBTN_exit.setOnClickListener(this);

        mBTN_setting = findViewById(R.id.activity_updating_btn_setting);
        mBTN_setting.setOnClickListener(this);


    }

    public void callGetTerminalInfo() {
        try {
            TerminalInfoDto terminalInfoDto = createTerminalInfoDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();

            new ApiCaller(Constant.Api.Type.TERMINAL_INFO).call(mContext, terminalInfoDto, AESsecretKey, null, new ApiCaller.ApiCallback() {
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        goToSuccessfulState();
                                    }
                                });

                                mTV_message.setText("منوی برنامه با موفقیت بروزرسانی شد");
                                goToMainMenu();
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
                    goToFailedState();
                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(UpdatingActivity.this, e, mClassName, "callGetTerminalInfo");
            goToFailedState();
        }

    }

    private void goToSuccessfulState() {
        mProgressView.upDateProgress(100);
        mProgressView.setCircularColor(Color.GREEN);
        mProgressView.setProgressColor(Color.GREEN);
        mProgressView.showDownloadOk();
    }

    private void goToUpdatingState() {
        // mProgressView.setVisibility(View.VISIBLE);
    }

    private void goToFailedState() {
        mBTN_retry.setEnabled(true);
        mBTN_exit.setEnabled(true);
        mBTN_exit.setEnabled(true);
        mProgressView.setCircularColor(Color.RED);
        mProgressView.setProgressColor(Color.RED);
        mProgressView.showDownloadError();
        mTV_message.setText("خطا در عملیات بروز رسانی");

    }

    private void goToMainMenu() {

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(UpdatingActivity.this, MainActivity.class);
                intent.putExtra(Constant.Key.UPDATE_MENU, true);
                startActivity(intent);
                finish();

            }
        }, 1000);

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
            List<PaymentModel> paymentModels = MenuMapper.convertPaymentStoToModels(menuStos);
            Db.Payment.insert(paymentModels);
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
            //session.setMerchantId(menuStos.get(0).dataModel.get(0).idMerchant);
            session.setMenuCategory(menuStos.get(0).dataModel.get(0).menuCategory);
            session.setBaseUrl(menuStos.get(0).dataModel.get(0).url);
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    doUpdate();
                }
            });
        } else if (id == mBTN_exit.getId()) {
            finish();
        } else if (id == mBTN_setting.getId()) {
            startActivity(new Intent(this, SettingLoginActivity.class));
            finish();
        }

    }
}
