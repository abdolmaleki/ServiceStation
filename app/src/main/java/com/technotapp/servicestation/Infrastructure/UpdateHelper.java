package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.Intent;

import com.technotapp.servicestation.activity.UpdatingActivity;
import com.technotapp.servicestation.setting.Session;

public class UpdateHelper {

    public static int lastVersion = 0;

    public static boolean checkNeedingUpdate(Context ctx) {
        int currentVersion = Session.getInstance(ctx).getAppVersion();
        if (lastVersion > currentVersion) {
            ctx.startActivity(new Intent(ctx, UpdatingActivity.class));
            return true;
        } else {
            return false;
        }
    }

    public static void setLastVersion(int version) {
        lastVersion = version;
    }
}
