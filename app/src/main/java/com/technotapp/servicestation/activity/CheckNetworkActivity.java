package com.technotapp.servicestation.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.Infrastructure.NetworkHelper;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.adapter.WifiAdapter;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.fragment.InputDialogFragment;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener;
import com.thanosfisherman.wifiutils.wifiState.WifiStateListener;

import java.util.List;

import io.ghyeok.stickyswitch.widget.StickySwitch;


public class CheckNetworkActivity extends BaseActivity implements AdapterView.OnItemClickListener, StickySwitch.OnSelectedChangeListener {

    private SwitchCompat mNetworkType;
    private LinearLayout mDataPanel;
    private LinearLayout mWifiPanel;
    StickySwitch mNetworkTypeSwitch;
    private ListView mListWifi;
    private WifiReceiver mWifiReceiver;
    private List<ScanResult> mLastScanResults;
    public static Handler mHandler;

    //    private SwitchCompat mWifiState;
//    private SwitchCompat mDataState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_network);

        loadData();
        initView();
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case 0: // no wifi connection
                        reDrawWifiList(null);
                        break;
                    case 1: // new wifi connected
                        reDrawWifiList(msg.obj.toString());
                        break;
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NetworkHelper.isWifiEnable) {
            onSelectedChange(StickySwitch.Direction.LEFT, "");
        } else if (NetworkHelper.isDataEnable) {
            onSelectedChange(StickySwitch.Direction.RIGHT, "");
        } else {
            onSelectedChange(mNetworkTypeSwitch.getDirection(), "");
        }

        registerReceiver(mWifiReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiReceiver);
    }

    private void initAdapter() {

        Helper.progressBar.showDialog(this, "در حال جستجو");
        WifiUtils.withContext(getApplicationContext()).scanWifi(new ScanResultsListener() {
            @Override
            public void onScanResults(@NonNull List<ScanResult> scanResults) {
                Helper.progressBar.hideDialog();
                mLastScanResults = scanResults;
                String currentSSID = NetworkHelper.getCurrentWifiSSID(CheckNetworkActivity.this);
                WifiAdapter wifiAdapter = new WifiAdapter(CheckNetworkActivity.this, scanResults, currentSSID);
                mListWifi.setAdapter(wifiAdapter);

            }
        }).start();
    }

    private void reDrawWifiList(String currentSSID) {
        if (mLastScanResults != null && mListWifi != null && mLastScanResults.size() > 0) {
            WifiAdapter wifiAdapter = new WifiAdapter(CheckNetworkActivity.this, mLastScanResults, currentSSID);
            mListWifi.setAdapter(wifiAdapter);
        }


    }

    private void initView() {
        mDataPanel = findViewById(R.id.activity_checknetwork_panel_data);
        mWifiPanel = findViewById(R.id.activity_checknetwork_panel_wifi);
        mListWifi = findViewById(R.id.activity_checknetwork_list_wifi);
        mNetworkTypeSwitch = findViewById(R.id.activity_checknetwork_switch_network_type);
        mNetworkTypeSwitch.setOnSelectedChangeListener(this);
        mListWifi.setOnItemClickListener(this);
        mWifiReceiver = new WifiReceiver();

    }

    private void loadData() {
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
                    Helper.progressBar.showDialog(CheckNetworkActivity.this, "در حال اتصال به " + result.SSID);
                    NetworkHelper.connectToWifi(CheckNetworkActivity.this, result.SSID, password, new ConnectionSuccessListener() {
                        @Override
                        public void isSuccessful(boolean isSuccess) {
                            Helper.progressBar.hideDialog();

                            if (isSuccess) {
                                Helper.progressBar.showDialog(CheckNetworkActivity.this, getString(R.string.updating));
                            } else {
                                Helper.alert(CheckNetworkActivity.this, "متصفانه اتصال برقرار نشد", Constant.AlertType.Error);

                            }

                        }
                    });


                }
            });
        } else { // no need password
            Helper.progressBar.showDialog(CheckNetworkActivity.this, "در حال اتصال به " + result.SSID);
            NetworkHelper.connectToWifi(this, result.SSID, "", new ConnectionSuccessListener() {
                @Override
                public void isSuccessful(boolean isSuccess) {
                    Helper.progressBar.hideDialog();

                    if (isSuccess) {
                        Helper.progressBar.showDialog(CheckNetworkActivity.this, getString(R.string.updating));
                    } else {
                        Helper.alert(CheckNetworkActivity.this, "متصفانه اتصال برقرار نشد", Constant.AlertType.Error);

                    }

                }
            });
        }


    }

    @Override
    public void onSelectedChange(StickySwitch.Direction direction, String s) {
        switch (direction) {
            case LEFT: //
                try {
                    mWifiPanel.setVisibility(View.VISIBLE);
                    mDataPanel.setVisibility(View.INVISIBLE);
                    NetworkHelper.enableWifi(CheckNetworkActivity.this, new WifiStateListener() {
                        @Override
                        public void isSuccess(boolean isSuccess) {
                            if (isSuccess) {
                                NetworkHelper.setMobileDataDisable(CheckNetworkActivity.this);
                                initAdapter();
                            }
                        }
                    });
                } catch (Exception e) {
                    AppMonitor.reportBug(this, e, "CheckNetworkActivity", "onSelectedChange");
                }

                break;
            case RIGHT: //data
                try {

                    mWifiPanel.setVisibility(View.INVISIBLE);
                    mDataPanel.setVisibility(View.VISIBLE);
                    NetworkHelper.setMobileDataEnabled(this, new NetworkHelper.DataEnableListener() {
                        @Override
                        public void onDataChangeState(boolean isOnSuccessfully) {
                            if (isOnSuccessfully) {
                                Helper.alert(CheckNetworkActivity.this, "اینترنت دیتا فعال شد", Constant.AlertType.Success);
                                NetworkHelper.disableWifi(CheckNetworkActivity.this);
                            } else {
                                //Todo
                            }
                        }
                    });
                } catch (Exception e) {
                    AppMonitor.reportBug(this, e, "CheckNetworkActivity", "onSelectedChange");
                }
                break;
        }
    }

    public static class WifiReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            Helper.progressBar.hideDialog();
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            Message message = new Message();

            if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Helper.alert(context, "اتصال با موفقیت انجام شد", Constant.AlertType.Success);
                String ssid = NetworkHelper.getCurrentWifiSSID(context);
                message.what = 1;
                message.obj = ssid;
                Log.e("WifiReceiver", "ssid: " + ssid);

            } else {
                message.what = 0;
                Log.e("WifiReceiver", "Don't have Wifi Connection");
            }

            mHandler.handleMessage(message);

        }
    }


}
