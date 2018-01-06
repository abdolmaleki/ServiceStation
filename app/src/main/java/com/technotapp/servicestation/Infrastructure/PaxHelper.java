package com.technotapp.servicestation.Infrastructure;

import android.content.Context;

import com.pax.dal.entity.ENavigationKey;
import com.pax.neptunelite.api.NeptuneLiteUser;


public class PaxHelper {
    private static NeptuneLiteUser neptuneLiteUser = NeptuneLiteUser.getInstance();

    public static void disableAllNavigationButton(Context ctx) {
        try {
            neptuneLiteUser.getDal(ctx).getSys().enableNavigationBar(false);
            neptuneLiteUser.getDal(ctx).getSys().showNavigationBar(false);
            neptuneLiteUser.getDal(ctx).getSys().enableStatusBar(true);
            neptuneLiteUser.getDal(ctx).getSys().showStatusBar(true);
        } catch (Exception e) {
            AppMonitor.reportBug(ctx, e, "PaxHelper", "disableAllNavigationButton");
        }
    }

    public static void enableBackNavigationButton(Context ctx) {
        try {
            neptuneLiteUser.getDal(ctx).getSys().showNavigationBar(true);
            neptuneLiteUser.getDal(ctx).getSys().enableNavigationBar(true);
        } catch (Exception e) {
            AppMonitor.reportBug(ctx, e, "PaxHelper", "disableBackNavigationButton");
        }
    }

    public static boolean isNavigationBarEnable(Context context) {
        try {
            return neptuneLiteUser.getDal(context).getSys().isNavigationBarEnabled() && neptuneLiteUser.getDal(context).getSys().isNavigationBarVisible();

        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "PaxHelper", "isNavigationBarEnable");
            return false;
        }

    }

}
