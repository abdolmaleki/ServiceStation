package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class AppMonitor {
    private static boolean isDebugMode = true;
    private static boolean isNeedUpdatingMenu = false;

    public static void reportBug(Exception ex, String className, String methodName) {

        AppMonitor.Log("Ex: " + ex.getMessage() + " class: " + className + " method: " + methodName);
    }

    public static void Log(String logStr) {
        if (isDebugMode) {
            Log.e("ServiceStation ----->", logStr);
        }
    }

    public static boolean isNeedMenuUpdate() {
        return isNeedUpdatingMenu;
    }

    public static void setMenuNeedingUpdate(boolean isNeedUpdating) {
        isNeedUpdatingMenu = isNeedUpdating;
    }

    public static void Toast(Context ctx, String text, int lenght) {
        Toast.makeText(ctx, text, lenght).show();
    }
}
