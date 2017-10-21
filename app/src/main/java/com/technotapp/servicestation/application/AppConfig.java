package com.technotapp.servicestation.application;

import android.app.Application;
import android.os.Environment;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.pax.printer.PrinterHelper;
import com.technotapp.servicestation.setting.Session;

import java.io.File;

public class AppConfig extends Application {

    public static IDAL idal = null;
    private Session mSession;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        copyFonts();

    }

    private void init() {
        mSession = new Session(this);
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
}
