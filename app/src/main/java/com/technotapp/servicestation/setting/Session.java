package com.technotapp.servicestation.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.technotapp.servicestation.application.Constant;


public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setIsFirstRun(boolean isFirstRun) {
        prefs.edit().putBoolean(Constant.Session.IS_FIRST_RUN, isFirstRun).apply();
    }


    public boolean IsFirstRun() {
        boolean isFirstRun = prefs.getBoolean(Constant.Session.IS_FIRST_RUN, true);
        return isFirstRun;
    }

    public void clear() {
//        prefs.edit().remove(Constant.Session.FirstName).apply();
        prefs.edit().clear();
        prefs.edit().commit();
    }
}
