package com.technotapp.servicestation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.Infrastructure.TransactionHelper;
import com.technotapp.servicestation.Infrastructure.UpdateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.MenuAdapterModel;
import com.technotapp.servicestation.adapter.MainMenuPageAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.GetVersionDto;
import com.technotapp.servicestation.connection.restapi.dto.LogDto;
import com.technotapp.servicestation.connection.restapi.dto.TerminalTransactionDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.connection.restapi.sto.BaseTransactionSto;
import com.technotapp.servicestation.database.Db;
import com.technotapp.servicestation.database.model.MenuModel;
import com.technotapp.servicestation.enums.ServiceType;
import com.technotapp.servicestation.fragment.MainGridFragment;
import com.technotapp.servicestation.fragment.PaymentListFragment;
import com.technotapp.servicestation.pax.printer.PrintMaker;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.activity_main_txt_shop_name)
    TextView txtShopName;
    @BindView(R.id.activity_main_viewPager)
    ViewPager viewPager;
    @BindView(R.id.activity_main_pagerIndicator)
    InkPageIndicator mIndicator;

    private Session mSession;
    private MainMenuPageAdapter mPagerAdapter;
    private Toast mToastMessage;
    private byte mCounter = 0;
    private boolean mIsFirst = true;
    private final String mClassName = getClass().getSimpleName();
    private Context mContext;
    public static final int NUMBER_OF_ITEMS = 9;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSetting();

        initDb();

        loadData();

        initView();

        initAdapter();

    }

    private void initDb() {
        Db.init(this);
    }

    private void loadSetting() {
    }

    private void loadData() {

    }

    //create gridViewPager
    private void initAdapter() {
        try {
            RealmResults<MenuModel> models = Db.Menu.getMainMenu();
            Iterator<MenuModel> it;
            List<MainGridFragment> gridFragments = new ArrayList<>();
            it = models.iterator();
            while (it.hasNext()) {
                ArrayList<MenuAdapterModel> imLst = new ArrayList<>();

                for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
                    if (it.hasNext()) {
                        MenuAdapterModel itm = new MenuAdapterModel(it.next());
                        imLst.add(itm);
                    } else break;
                }
                gridFragments.add(MainGridFragment.newInstance(imLst));
            }
            mPagerAdapter = new MainMenuPageAdapter(getSupportFragmentManager(), gridFragments);
            viewPager.setAdapter(mPagerAdapter);
            mIndicator.setViewPager(viewPager);
        } catch (Exception e) {
            AppMonitor.reportBug(this, e, mClassName, "initAdapter");
        }
    }

    private void initView() {
        try {
            ButterKnife.bind(this);
            mContext = MainActivity.this;
            mSession = Session.getInstance(this);
            findViewById(R.id.activity_main_img_buy).setOnClickListener(this);

        } catch (Exception e) {
            AppMonitor.reportBug(this, e, mClassName, "initView");
        }
    }

    //open settingActivity after 10th click on logo
    public void openSettingMenu(View view) {
        try {
            if (mToastMessage != null) {
                mToastMessage.cancel();
            }

            if (mCounter == 9) {
                mCounter = 0;
                startActivity(new Intent(mContext, SettingLoginActivity.class));
                return;
            } else if (mCounter > 3) {
                mToastMessage = Toast.makeText(mContext, "شما " + (9 - mCounter) + " قدمی دسترسی به تنظیمات هستید.", Toast.LENGTH_SHORT);
                mToastMessage.show();
            }
            mCounter++;
            if (mIsFirst) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mCounter = 0;
                        mIsFirst = false;
                    }
                }, 10000);

            }
        } catch (Exception e) {
            AppMonitor.reportBug(this, e, mClassName, "openSettingMenu");
        }
    }

    public void callSendLog() {
        try {
            LogDto logDto = createLogDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.LOG_INFO).call(mContext, logDto, AESsecretKey, null, new ApiCaller.ApiCallback() {
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
                                UpdateHelper.checkNeedingUpdate(MainActivity.this);
                            } else {
                                Helper.alert(MainActivity.this, sto.get(0).messageModel.get(0).errorString, Constant.AlertType.Error);
                            }
                        } else {
                            Helper.alert(MainActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(MainActivity.this, e, "MainActivity", "callSendLog-onResponse");
                        Helper.alert(MainActivity.this, getString(R.string.api_data_download_error), Constant.AlertType.Error);

                    }
                }

                @Override
                public void onFail(String message) {

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(MainActivity.this, e, mClassName, "callSendLog");
        }
    }

    public void callGetVersion() {
        try {
            GetVersionDto getVersionDto = createVersionDto();
            final SecretKey AESsecretKey = Encryptor.generateRandomAESKey();
            new ApiCaller(Constant.Api.Type.GET_VERSION).call(mContext, getVersionDto, AESsecretKey, null, new ApiCaller.ApiCallback() {
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
                                UpdateHelper.checkNeedingUpdate(MainActivity.this);
                            } else {
                            }
                        } else {
                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(MainActivity.this, e, "MainActivity", "callGetVersion-onResponse");

                    }
                }

                @Override
                public void onFail(String message) {

                }
            });
        } catch (Exception e) {
            AppMonitor.reportBug(MainActivity.this, e, mClassName, "callGetVersion");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        callGetVersion();
        txtShopName.setText(mSession.getShopName());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isMustExit = intent.getBooleanExtra(Constant.Key.EXIT, false);
            if (isMustExit) {
                PaxHelper.enableBackNavigationButton(MainActivity.this);
                finish();
            }

            boolean isMenuUpdate = intent.getBooleanExtra(Constant.Key.UPDATE_MENU, false);
            if (isMenuUpdate) {
                initAdapter();
            }

        }
    }

    private LogDto createLogDto() {

        LogDto dto = new LogDto();
        dto.Description = DateHelper.getShamsiDate();
        dto.Title = "";
        dto.UserDeviceInfo = "My Pos Info";
        dto.LogTypeId = 1;
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        return dto;
    }

    private GetVersionDto createVersionDto() {
        GetVersionDto dto = new GetVersionDto();
        dto.tokenId = mSession.getTokenId();
        dto.terminalCode = mSession.getTerminalId();
        return dto;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_main_img_buy) {
            TerminalTransactionDto dto = new TerminalTransactionDto();
            dto.transactionModel.amountOfTransaction = -1;
            TransactionHelper.startServiceTransaction(this, ServiceType.BUY_PRODUCT, dto, new PaymentListFragment.PaymentResultListener() {
                @Override
                public void onSuccessfullPayment(String message, BaseTransactionSto response) {
                    Helper.alert(MainActivity.this, message, Constant.AlertType.Success);
                    PrintMaker.startFactorPrint(MainActivity.this, response);
                }

                @Override
                public void onFailedPayment(String message, BaseTransactionSto baseTransactionSto) {
                    Helper.alert(MainActivity.this, message, Constant.AlertType.Error);
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }
}
