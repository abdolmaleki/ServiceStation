package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.technotapp.servicestation.activity.CheckNetworkActivity;
import com.technotapp.servicestation.application.Constant;

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

    public static boolean isConnectingToInternet(Context ctx) {

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
            AppMonitor.reportBug(e, "NetworkHelper", "isConnectingToInternet");
            Helper.alert(ctx, "مشکل عدم ارتباط با اینترنت", Constant.AlertType.Error);
            return false;
        }
        Helper.alert(ctx, "مشکل عدم ارتباط با اینترنت", Constant.AlertType.Error);
        return false;
    }


}
