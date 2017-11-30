package com.technotapp.servicestation.activity;

import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.NetworkHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.WifiAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.fragment.InputDialogFragment;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener;
import com.thanosfisherman.wifiutils.wifiState.WifiStateListener;

import java.util.List;


public class CheckNetworkActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {

    private SwitchCompat mNetworkType;
    private SwitchCompat mWifiState;
    private SwitchCompat mDataState;
    private LinearLayout mDataPanel;
    private LinearLayout mWifiPanel;
    private ListView mListWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_network);

        loadData();
        initView();
    }

    private void initAdapter() {

        WifiUtils.withContext(getApplicationContext()).scanWifi(new ScanResultsListener() {
            @Override
            public void onScanResults(@NonNull List<ScanResult> scanResults) {
                WifiAdapter wifiAdapter = new WifiAdapter(CheckNetworkActivity.this, scanResults);
                mListWifi.setAdapter(wifiAdapter);
            }
        }).start();
    }

    private void initView() {
        mNetworkType = findViewById(R.id.activity_checknetwork_switch_nettype);
        mNetworkType.setOnCheckedChangeListener(this);
        mWifiState = findViewById(R.id.activity_checknetwork_switch_wifi_state);
        mWifiState.setOnCheckedChangeListener(this);
        mDataState = findViewById(R.id.activity_checknetwork_switch_data_state);
        mDataState.setOnCheckedChangeListener(this);
        mDataPanel = findViewById(R.id.activity_checknetwork_panel_data);
        mWifiPanel = findViewById(R.id.activity_checknetwork_panel_wifi);
        mListWifi = findViewById(R.id.activity_checknetwork_list_wifi);
        mListWifi.setOnItemClickListener(this);

    }

    private void loadData() {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        int id = compoundButton.getId();

        switch (id) {
            case R.id.activity_checknetwork_switch_nettype:
                if (isChecked) {
                    mWifiPanel.setVisibility(View.INVISIBLE);
                    mDataPanel.setVisibility(View.VISIBLE);
                } else {
                    mWifiPanel.setVisibility(View.VISIBLE);
                    mDataPanel.setVisibility(View.INVISIBLE);
                }
                break;

            case R.id.activity_checknetwork_switch_wifi_state:
                if (isChecked) {
                    NetworkHelper.enableWifi(this, new WifiStateListener() {
                        @Override
                        public void isSuccess(boolean isSuccess) {
                            NetworkHelper.isWifiEnable = isSuccess;
                            if (isSuccess) {
                                initAdapter();
                            }
                        }
                    });

                } else {
                    NetworkHelper.disableWifi(this);
                }
                break;

            case R.id.activity_checknetwork_switch_data_state:
                if (isChecked) {
                    NetworkHelper.setMobileDataEnabled(this, true, new NetworkHelper.DataEnableListener() {
                        @Override
                        public void onDataChangeState(boolean isOnSuccessfully) {
                            if (isOnSuccessfully) {
                                Helper.alert(CheckNetworkActivity.this, "اینترنت دیتا فعال شد", Constant.AlertType.Success);
                            } else {
                                Helper.alert(CheckNetworkActivity.this, "متاسفانه امکان فعالسازی اینترنت دیتا وجود ندارد", Constant.AlertType.Error);
                                mDataState.setChecked(false);

                            }
                        }
                    });
                } else {
                }
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ScanResult result = (ScanResult) adapterView.getItemAtPosition(i);
        if (NetworkHelper.isProtectedWifi(result)) { // wifi need password
            InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance();
            inputDialogFragment.show(getFragmentManager(), "inputDialog");
            inputDialogFragment.setOnInputDialogClickListener(new InputDialogFragment.OnInputDialogClick() {
                @Override
                public void onAccept(String password) {
                    Helper.alert(CheckNetworkActivity.this, "در حال اتصال به " + result.SSID,Constant.AlertType.Loading);
                    NetworkHelper.connectToWifi(CheckNetworkActivity.this, result.SSID, password, new ConnectionSuccessListener() {
                        @Override
                        public void isSuccessful(boolean isSuccess) {
                            Helper.dismiss(Constant.AlertType.Loading);
                            if (isSuccess) {
                                Helper.alert(CheckNetworkActivity.this, "اتصال با موفقیت انجام شد", Constant.AlertType.Success);
                            } else {
                                Helper.alert(CheckNetworkActivity.this, "متصفانه اتصال برقرار نشد", Constant.AlertType.Error);

                            }

                        }
                    });


                }
            });
        } else {
            Helper.alert(CheckNetworkActivity.this, "در حال اتصال به " + result.SSID,Constant.AlertType.Loading);
            NetworkHelper.connectToWifi(this, result.SSID, "", new ConnectionSuccessListener() {
                @Override
                public void isSuccessful(boolean isSuccess) {
                    Helper.dismiss(Constant.AlertType.Loading);
                    if (isSuccess) {
                        Helper.alert(CheckNetworkActivity.this, "اتصال با موفقیت انجام شد", Constant.AlertType.Success);
                    } else {
                        Helper.alert(CheckNetworkActivity.this, "متصفانه اتصال برقرار نشد", Constant.AlertType.Error);

                    }

                }
            });
        }


    }
}
