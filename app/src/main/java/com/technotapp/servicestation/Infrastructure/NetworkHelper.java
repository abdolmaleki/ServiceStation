package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiState.WifiStateListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.WIFI_SERVICE;

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
                        HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com/").openConnection());
                        urlc.setRequestProperty("User-Agent", "Android");
                        urlc.setRequestProperty("Connection", "close");
                        urlc.setConnectTimeout(2000);
                        urlc.connect();
                        if ((urlc.getResponseCode() == 200)) {
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

    //
    // __        _____ _____ ___    __  __                                              _
    // \ \      / /_ _|  ___|_ _|  |  \/  | __ _ _ __   __ _  __ _ _ __ ___   ___ _ __ | |_
    //  \ \ /\ / / | || |_   | |   | |\/| |/ _` | '_ \ / _` |/ _` | '_ ` _ \ / _ \ '_ \| __|
    //   \ V  V /  | ||  _|  | |   | |  | | (_| | | | | (_| | (_| | | | | | |  __/ | | | |_
    //    \_/\_/  |___|_|   |___|  |_|  |_|\__,_|_| |_|\__,_|\__, |_| |_| |_|\___|_| |_|\__|
    //                                                       |___/

    public static void enableWifi(Context ctx, WifiStateListener wifiStateListener) {
        try {
            WifiUtils.withContext(ctx.getApplicationContext()).enableWifi(wifiStateListener);
            NetworkHelper.isWifiEnable = true;
        } catch (Exception e) {
            NetworkHelper.isWifiEnable = false;
            AppMonitor.reportBug(e, "NetworkHlper", "enableWifi");

        }

    }

    public static void disableWifi(Context ctx) {
        WifiUtils.withContext(ctx.getApplicationContext()).disableWifi();
        NetworkHelper.isWifiEnable = false;
    }

    public static String getCurrentWifiSSID(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
                return ssid.substring(1, ssid.length() - 1);

            }
        }
        return null;
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

    public static boolean isWifiAutoLogined(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();

        } catch (Exception e) {
            AppMonitor.reportBug(e, "NetworkHelper", "isWifiAutoLogined");
            return false;
        }
    }

    public static WifiInfo getCurrentWifiInfo(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
            return wifiManager.getConnectionInfo();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "NetworkHelper", "getCurrentWifiInfo");
            return null;
        }
    }

    //
    //  ____        _           __  __                                              _
    // |  _ \  __ _| |_ __ _   |  \/  | __ _ _ __   __ _  __ _ _ __ ___   ___ _ __ | |_
    // | | | |/ _` | __/ _` |  | |\/| |/ _` | '_ \ / _` |/ _` | '_ ` _ \ / _ \ '_ \| __|
    // | |_| | (_| | || (_| |  | |  | | (_| | | | | (_| | (_| | | | | | |  __/ | | | |_
    // |____/ \__,_|\__\__,_|  |_|  |_|\__,_|_| |_|\__,_|\__, |_| |_| |_|\___|_| |_|\__|
    //                                                   |___/

    public static void setMobileDataEnabled(Context context, DataEnableListener dataEnableListener) {
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
                setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
                dataEnableListener.onDataChangeState(true);
                NetworkHelper.isDataEnable = true;

            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
            dataEnableListener.onDataChangeState(false);
            NetworkHelper.isDataEnable = false;

        }
    }

    public static void setMobileDataDisable(Context context) {
        try {
            if (isSimcardAvalaible(context)) {
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                final Class conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, false);
                NetworkHelper.isDataEnable = false;
            }
        } catch (Exception e) {
            NetworkHelper.isDataEnable = false;
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataDisable");
        }
    }

    public interface DataEnableListener {
        void onDataChangeState(boolean isOnSuccessfully);
    }

    private static boolean isSimcardAvalaible(Context context) {
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
