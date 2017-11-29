package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.technotapp.servicestation.application.Constant;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiScan.ScanResultsListener;
import com.thanosfisherman.wifiutils.wifiState.WifiStateListener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkHelper {

    public static boolean isWifiEnable;
    public static boolean isDataEnable;

    public static boolean isConnectingToInternet(Context ctx) {
        return true;
//        if (isNetworkAvailable(ctx)) {
//            try {
//                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://clients3.google.com/generate_204").openConnection());
//                urlc.setRequestProperty("User-Agent", "Android");
//                urlc.setRequestProperty("Connection", "close");
//                urlc.setConnectTimeout(2000);
//                urlc.connect();
//                return (urlc.getResponseCode() == 204 && urlc.getContentLength() == 0);
//            } catch (Exception e) {
//                AppMonitor.reportBug(e, "NetworkHelper", "isConnectingToInternet");
//                Helper.alert(ctx, "مشکل عدم ارتباط با اینترنت", Constant.AlertType.Error);
//                return false;
//            }
//        } else {
//            Helper.alert(ctx, "لطفا اینترنت وای فای یا سیم کارت خود را فعال کنید", Constant.AlertType.Error);
//            AppMonitor.Log("لطفا اینترنت وای فای یا سیم کارت خود را فعال کنید");
//        }
//        return false;

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
    }

    public static List<ScanResult> scanWifi(Context ctx) {

        List<ScanResult> wifiList = new ArrayList<>();
        WifiUtils.withContext(ctx.getApplicationContext()).scanWifi(new ScanResultsListener() {
            @Override
            public void onScanResults(@NonNull List<ScanResult> scanResults) {
                wifiList.addAll(scanResults);
            }
        }).start();
        return wifiList;
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
        return "نامشخص";
    }

    public static void connectToWifi(Context context, String ssid, ConnectionSuccessListener connectionSuccessListener) {
        WifiUtils.withContext(context.getApplicationContext())
                .connectWith(ssid, "")
                .onConnectionResult(connectionSuccessListener)
                .start();
    }


}
