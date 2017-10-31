package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.technotapp.servicestation.activity.CheckNetworkActivity;
import com.technotapp.servicestation.activity.SigninActivity;
import com.technotapp.servicestation.activity.SplashActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
}
