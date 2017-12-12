package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.Intent;

import com.technotapp.servicestation.activity.UpdatingActivity;
import com.technotapp.servicestation.setting.Session;

public class UpdateHelper {

    private static Session mSession;

    public static boolean checkNeedingUpdate(Context ctx) {
        mSession = Session.getInstance(ctx);
        int currentVersion = mSession.getAppVersion();
        int lastVersion = mSession.getLastVersion();
        if (lastVersion > currentVersion) {
            mSession.setIsNewMenu(true);
            ctx.startActivity(new Intent(ctx, UpdatingActivity.class));
            return true;
        } else {
            return false;
        }
    }
}
