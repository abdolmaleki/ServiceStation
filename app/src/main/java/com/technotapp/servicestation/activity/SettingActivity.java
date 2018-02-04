package com.technotapp.servicestation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Encryptor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.UpdateHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.DataModel.SettingAdapterModel;
import com.technotapp.servicestation.adapter.SettingAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.ApiCaller;
import com.technotapp.servicestation.connection.restapi.dto.GetVersionDto;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.enums.SettingActionType;
import com.technotapp.servicestation.enums.UserRole;
import com.technotapp.servicestation.setting.Session;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {


    private Session mSession;
    private ImageView mBack;
    private ImageView mExit;
    private GridView mGridMenu;
    UserRole mUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        loadData();
        initView();
        initAdapter();
    }

    private void initAdapter() {
        ArrayList<SettingAdapterModel> adapterModels = new ArrayList<>();
        adapterModels.add(new SettingAdapterModel("پیکره بندی", R.drawable.ic_config, SettingActionType.CONFIG));
        adapterModels.add(new SettingAdapterModel("خدمات و کالاها", R.drawable.ic_product, SettingActionType.PRODUCT));
        adapterModels.add(new SettingAdapterModel("اینترنت", R.drawable.ic_internet, SettingActionType.INTERNET));
        if (mUserRole == UserRole.MERCHANT) {
            adapterModels.add(new SettingAdapterModel("گزارش تراکنش ها", R.drawable.ic_transaction_setting, SettingActionType.TRANSACTIONS));
            adapterModels.add(new SettingAdapterModel("گزارش فاکتورها", R.drawable.ic_invoice, SettingActionType.FACTOR));
            adapterModels.add(new SettingAdapterModel("پیشنهادات", R.drawable.ic_offer, SettingActionType.OFFERS));
        }
        if (mUserRole == UserRole.SUPPORTER) {
            adapterModels.add(new SettingAdapterModel("گزارش پشتیبانی", R.drawable.ic_report, SettingActionType.REPORT));
        }
        adapterModels.add(new SettingAdapterModel("چاپگر", R.drawable.ic_printer, SettingActionType.PRINTER));
        adapterModels.add(new SettingAdapterModel("ویرایش مشخصات", R.drawable.ic_edit_profile, SettingActionType.PROFILE));
        adapterModels.add(new SettingAdapterModel("بروزرسانی", R.drawable.ic_update_setting, SettingActionType.UPDATE));
        adapterModels.add(new SettingAdapterModel("درباره نرم افزار", R.drawable.ic_about, SettingActionType.ABOUT));

        SettingAdapter adapter = new SettingAdapter(this, adapterModels);
        mGridMenu.setAdapter(adapter);
        mGridMenu.setOnItemClickListener(this);
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
        mSession = Session.getInstance(this);
        mGridMenu = findViewById(R.id.activity_setting_grid_view_menu);
        ImageView mUserImage = findViewById(R.id.activity_setting_img_user);
        mExit = findViewById(R.id.activity_setting_img_exit);
        mExit.setOnClickListener(this);
        mBack = findViewById(R.id.activity_setting_img_back);
        mBack.setOnClickListener(this);


        Glide.with(this).load("https://media.licdn.com/mpr/mpr/shrinknp_200_200/p/3/000/076/07f/0d01ca8.jpg")
                .apply(RequestOptions.circleCropTransform())
                .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(mUserImage)
                .onLoadFailed(getResources().getDrawable(R.drawable.ic_loading));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == mBack.getId()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == mExit.getId()) {
            exitApp();
        }
    }

    private void exitApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constant.Key.EXIT, true);
        startActivity(intent);
        finish();
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
                                } else {
                                    finish();
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
                public void onFail(String message) {
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long actionType) {
        switch ((int) actionType) {
            case SettingActionType.CONFIG:
                startActivity(new Intent(this, ConfigActivity.class));
                break;
            case SettingActionType.PRODUCT:
                startActivity(new Intent(this, ProductManagementActivity.class));
                break;
            case SettingActionType.INTERNET:
                startActivity(new Intent(this, CheckNetworkActivity.class));
                break;
            case SettingActionType.TRANSACTIONS:
                startActivity(new Intent(this, SearchTransactionActivity.class));
                break;
            case SettingActionType.FACTOR:
                startActivity(new Intent(this, SearchFactorActivity.class));
                break;
            case SettingActionType.OFFERS:
                startActivity(new Intent(this, SuggestionActivity.class));
                break;
            case 6:
                break;
            case 7:
                break;
            case SettingActionType.PROFILE:
                startActivity(new Intent(this, EditProfileActivity.class));
                break;
            case SettingActionType.UPDATE:
                checkUpdate();
                break;

            case SettingActionType.ABOUT:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }
}
