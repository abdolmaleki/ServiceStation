package com.technotapp.servicestation.Infrastructure;

import android.util.Log;

import com.pax.dal.IDAL;
import com.technotapp.servicestation.application.AppConfig;

public class GetObj {

    static String logStr = "";

    public static IDAL getDal() {
        IDAL dal = AppConfig.idal;
        if (dal == null) {
            Log.e("NeptuneLiteDemo", "dal is null");
        }
        return dal;
    }

}
