package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener;
import com.thanosfisherman.wifiutils.wifiState.WifiStateListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkHelper {

    public static boolean isWifiEnable;
    public static boolean isDataEnable;

    public static void isConnectingToInternet(Context ctx, CheckNetworkStateListener checkNetworkStateListener) {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                Looper.prepare();

                if (isNetworkAvailable(ctx)) {
                    try {
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
                        urlc.setRequestProperty("User-Agent", "Android");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(2000);
                        urlc.connect();
                        if ((urlc.getResponseCode() == 204 && urlc.getContentLength() == 0)) {
                            checkNetworkStateListener.onNetworkChecked(true, "اتصال موفقیت آمیز به اینترنت");

                        } else {
                            checkNetworkStateListener.onNetworkChecked(false, "اشکال در اتصال به اینترنت");

                        }
                    } catch (Exception e) {
                        AppMonitor.reportBug(e, "NetworkHelper", "isConnectingToInternet");
                        checkNetworkStateListener.onNetworkChecked(false, "مشکل اتصال با اینترنت");
                    }
                } else {
                    checkNetworkStateListener.onNetworkChecked(false, "لطفا اینترنت وای فای یا سیم کارت خود را فعال کنید");
                }
            }
        };

        new Thread(runnable).start();

    }

    private static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager
                    cm = (ConnectivityManager) context.getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();

        } catch (Exception e) {
            AppMonitor.reportBug(e, "NetworkHelper", "isNetworkAvailable");
            return false;
        }
    }

    public static void enableWifi(Context ctx, WifiStateListener wifiStateListener) {
        WifiUtils.withContext(ctx.getApplicationContext()).enableWifi(wifiStateListener);
    }

    public static void disableWifi(Context ctx) {
        WifiUtils.withContext(ctx.getApplicationContext()).disableWifi();
        NetworkHelper.isWifiEnable = false;
    }


    public static boolean isProtectedWifi(ScanResult scanResult) {
        final String cap = scanResult.capabilities;
        final String[] securityModes = {"WEP", "WPA", "WPA2", "WPA_EAP", "IEEE8021X"};
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                return true;
            }
        }

        return false;
    }

    public static String getWifiSignalQuality(int level) {

        if (level < 0 && level > -50) {
            return "خوب";
        } else if (level < -50 && level > -85) {
            return "متوسط";

        } else if (level < -85) {
            return "ضعیف";
        }
        return "ضعیف";
    }

    public static void connectToWifi(Context context, String ssid, String password, ConnectionSuccessListener connectionSuccessListener) {
        WifiUtils.withContext(context.getApplicationContext())
                .connectWith(ssid, password)
                .onConnectionResult(connectionSuccessListener)
                .start();
    }

    public interface CheckNetworkStateListener {
        void onNetworkChecked(boolean isSuccess, String message);
    }

    public static void setMobileDataEnabled(Context context, boolean enabled, DataEnableListener dataEnableListener) {
        try {
            if (!isSimcardAvalaible(context)) {
                dataEnableListener.onDataChangeState(false);
            } else {
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final Class conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
                dataEnableListener.onDataChangeState(true);
                if (enabled) {
                    NetworkHelper.isDataEnable = true;
                } else {
                    NetworkHelper.isDataEnable = false;
                }

            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
            dataEnableListener.onDataChangeState(false);
        }
    }

    public interface DataEnableListener {
        void onDataChangeState(boolean isOnSuccessfully);
    }

    public static boolean isSimcardAvalaible(Context context) {
        try {
            TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int simState = telMgr.getSimState();
            switch (simState) {

                case TelephonyManager.SIM_STATE_READY:
                    return true;
                default:
                    return false;

            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "NetworkHelper", "isSimcardAvalaible");
            return false;

        }

    }


}
