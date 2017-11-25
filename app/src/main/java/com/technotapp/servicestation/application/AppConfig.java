package com.technotapp.servicestation.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.pax.printer.PrinterHelper;
import com.technotapp.servicestation.setting.Session;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppConfig extends Application {
    public static IDAL idal = null;
    private Session mSession;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PaxHelper.disableAllNavigationButton(getApplicationContext());
        init();
        //copyFonts();
        configDb();

    }

    private void init() {
        mSession = Session.getInstance(this);
        initIDAL();
    }

    private void initIDAL() {
        try {
            idal = NeptuneLiteUser.getInstance().getDal(getApplicationContext());
        } catch (Exception e) {
            AppMonitor.reportBug(e, "AppConfig", "initIDAL");
        }
    }

    private void copyFonts() {
        try {
            if (mSession.IsFirstRun()) {
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fonts");
                if (!file.isDirectory()) {
                    file.mkdirs();
                }
                PrinterHelper.copyFontFromAssets(this);
                mSession.setIsFirstRun(false);
            }
        } catch (Exception e) {
            AppMonitor.reportBug(e, "AppConfig", "copyFonts");
        }
    }

    private void configDb() {
        try {
            Realm.init(this);

            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name("service_station.realm")
                    .schemaVersion(0)
                    .deleteRealmIfMigrationNeeded()
                    .build();

            Realm.setDefaultConfiguration(config);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "", "");
        }

    }
}
