package com.technotapp.servicestation.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.PaxHelper;
import com.technotapp.servicestation.setting.Session;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AppConfig extends MultiDexApplication {
    public static IDAL idal = null;
    private Session mSession;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //PaxHelper.disableAllNavigationButton(getApplicationContext());
        init();
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
            AppMonitor.reportBug(this, e, "AppConfig", "initIDAL");
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
            AppMonitor.reportBug(this, e, "AppConfig", "configDb");
        }

    }
}
