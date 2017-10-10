package com.technotapp.servicestation.application;

import android.app.Application;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.AppMonitor;

public class AppConfig extends Application {

    public static IDAL idal = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initIDAL();

    }

    private void initIDAL() {
        try {
            idal = NeptuneLiteUser.getInstance().getDal(getApplicationContext());
        } catch (Exception e) {
            AppMonitor.reportBug(e, "AppConfig", "initIDAL");
        }
    }
}
