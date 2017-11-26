package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.Intent;

import com.technotapp.servicestation.activity.UpdatingActivity;
import com.technotapp.servicestation.setting.Session;

public class UpdateHelper {

    public static boolean checkNeedingUpdate(Context ctx) {
        int currentVersion = Session.getInstance(ctx).getAppVersion();
        int lastVersion = Session.getInstance(ctx).getLastVersion();
        if (lastVersion > currentVersion) {
            ctx.startActivity(new Intent(ctx, UpdatingActivity.class));
            return true;
        } else {
            return false;
        }
    }
}
