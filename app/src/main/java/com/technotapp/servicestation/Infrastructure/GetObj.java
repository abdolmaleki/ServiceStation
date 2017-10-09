package com.technotapp.servicestation.Infrastructure;

import android.util.Log;

import com.pax.dal.IDAL;

public class GetObj {

    private static IDAL mDal;
    public static String logStr = "";

    public static IDAL getDal(IDAL dal) {
        mDal = dal;
        if (mDal == null) {
            Log.e("NeptuneLiteDemo", "dal is null");
        }
        return mDal;
    }

}
