package com.technotapp.servicestation.Infrastructure;

import android.util.Log;

public class AppMonitor {
    private static boolean isDebugMode = true;

    public static void reportBug(Exception ex, String className, String methodName) {
    }

    public static void Log(String logStr) {
        if (isDebugMode) {
            Log.e("ServiceStation ----->", logStr);
        }

    }
}
