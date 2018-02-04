package com.technotapp.servicestation.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.technotapp.servicestation.application.Constant;

import java.util.Calendar;


public class AppSetting {

    private static SharedPreferences prefs;
    private static AppSetting session;

    public static AppSetting getInstance(Context cntx) {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
        }
        if (session == null) {
            session = new AppSetting();
        }

        return session;
    }

    public void setIsTurningEnable(boolean isTurningEnable) {
        prefs.edit().putBoolean(Constant.Setting.IS_TURNING_ENABLED, isTurningEnable).apply();
    }

    public void setSellerPrintEnabled(boolean isSellerPrintEnabled) {
        prefs.edit().putBoolean(Constant.Setting.IS_SELLER_PRINT_ENABLED, isSellerPrintEnabled).apply();
    }

    public void setCustomerPrintEnabled(boolean isCustomerPrintEnabled) {
        prefs.edit().putBoolean(Constant.Setting.IS_CUSOTMER_PRINT_ENABLED, isCustomerPrintEnabled).apply();
    }

    public void setTurnRate(long turnRate) {
        prefs.edit().putLong(Constant.Setting.TURN_RATE, turnRate).apply();
    }


    public void setLastTurningDate(long lastTurningDate) {
        prefs.edit().putLong(Constant.Setting.LAST_TURNING_DATE, lastTurningDate).apply();
    }

    public boolean isTurningEnabled() {
        boolean isTurningEnabled = prefs.getBoolean(Constant.Setting.IS_TURNING_ENABLED, false);
        return isTurningEnabled;
    }

    public boolean isSellerPrintEnabled() {
        boolean isSellerPrintEnabled = prefs.getBoolean(Constant.Setting.IS_SELLER_PRINT_ENABLED, false);
        return isSellerPrintEnabled;
    }

    public boolean isCustomerPrintEnabled() {
        boolean isCustomerPrintEnabled = prefs.getBoolean(Constant.Setting.IS_CUSOTMER_PRINT_ENABLED, false);
        return isCustomerPrintEnabled;
    }


    public long getLastTurningDate() {
        long LastTurningDate = prefs.getLong(Constant.Setting.LAST_TURNING_DATE, -1);
        return LastTurningDate;
    }

    public long getTurnRate() {
        long turnRate = prefs.getLong(Constant.Setting.TURN_RATE, 0);
        return turnRate;
    }

    public void clear() {
        prefs.edit().clear();
        prefs.edit().apply();
    }

}
