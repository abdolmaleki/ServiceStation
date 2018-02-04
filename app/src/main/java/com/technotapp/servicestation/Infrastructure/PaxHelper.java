package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.os.RemoteException;
import android.provider.Settings;

import com.ibm.icu.util.Calendar;
import com.pax.dal.entity.ENavigationKey;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.setting.AppSetting;


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

    public static void setBrightness(Context context, int brightness) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);

        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "PaxHelper", "setBrightness");
        }
    }

    public static int getDeviceBrighness(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 0);

        } catch (Exception e) {
            AppMonitor.reportBug(context, e, "PaxHelper", "getDeviceBrighness");
            return 0;
        }
    }

    public static long generateTurnRating(Context context) {

        AppSetting appSetting = AppSetting.getInstance(context);

        long lastTurningDate = appSetting.getLastTurningDate();
        long currentDate = Calendar.getInstance().getTimeInMillis();

        if (lastTurningDate == -1 || DateHelper.differenceDateInDay(lastTurningDate, currentDate) > 0) { // New Day
            appSetting.setLastTurningDate(currentDate);
            long rate = 1;
            appSetting.setTurnRate(rate + 1);
            return rate;
        } else { // CurrentDay
            long rate = appSetting.getTurnRate();
            appSetting.setTurnRate(rate + 1);
            return rate;
        }


    }

}
