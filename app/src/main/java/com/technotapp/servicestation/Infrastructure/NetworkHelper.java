package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.technotapp.servicestation.activity.CheckNetworkActivity;

public class NetworkHelper {

    public static boolean checkNetwork(Context ctx) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            } else {
                ctx.startActivity(new Intent(ctx, CheckNetworkActivity.class));
                return false;
            }

        } catch (Exception e) {
            AppMonitor.reportBug(e, "NetworkHelper", "checkNetwork");
            return false;
        }
    }

    public boolean isConnectingToInternet(Context ctx) {

        try {
            ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }

            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, this.getClass().getName(), "isConnectingToInternet");
            return false;
        }

        return false;
    }


}
