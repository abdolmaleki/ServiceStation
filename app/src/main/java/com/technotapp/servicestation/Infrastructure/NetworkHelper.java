package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.net.ConnectivityManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NetworkHelper {


    private void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class conmanClass = null;
        try {
            conmanClass = Class.forName(conman.getClass().getName());
        } catch (ClassNotFoundException e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
        }
        Field connectivityManagerField = null;
        try {
            connectivityManagerField = conmanClass.getDeclaredField("mService");
        } catch (NoSuchFieldException e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
        }
        connectivityManagerField.setAccessible(true);
        Object connectivityManager = null;
        try {
            connectivityManager = connectivityManagerField.get(conman);
        } catch (IllegalAccessException e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
        }
        Class connectivityManagerClass = null;
        try {
            connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
        } catch (ClassNotFoundException e) {

            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
        }
        Method setMobileDataEnabledMethod = null;
        try {
            setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        } catch (NoSuchMethodException e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
        }
        setMobileDataEnabledMethod.setAccessible(true);
        try {
            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
        } catch (IllegalAccessException e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
        } catch (InvocationTargetException e) {
            AppMonitor.reportBug(e, "NetworkHelper", "setMobileDataEnabled");
        }
    }

}
