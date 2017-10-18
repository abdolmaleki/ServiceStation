package com.technotapp.servicestation.Infrastructure;

import android.util.Log;

public class TestLog {

    private String childName = "";

    protected TestLog() {
        
    }

    protected void logTrue(String method) {
        childName = getClass().getSimpleName() + ".";
        String trueLog = childName + method;
        Log.i("IPPITest", trueLog);
        //clear();
        GetObj.logStr += ("true:"+trueLog + "\n");
    }

    protected void logErr(String method, String errString) {
        childName = getClass().getSimpleName() + ".";
        String errorLog = childName + method + "   errorMessage：" + errString;
        Log.e("IPPITest", errorLog);
        //clear();
        GetObj.logStr += ("error:"+errorLog + "\n");
    }

    public void clear() {
        GetObj.logStr = "";
    }

    public String getLog() {
        return GetObj.logStr.equals("") ? "Log" : GetObj.logStr;
    }

}
