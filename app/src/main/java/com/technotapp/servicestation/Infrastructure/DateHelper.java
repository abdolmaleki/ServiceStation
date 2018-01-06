package com.technotapp.servicestation.Infrastructure;

import android.util.Log;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static String getGregorianDateTime(String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            Date date = new Date();
            return (dateFormat.format(date));
        } catch (Exception e) {
            Log.e("DateHelper", "getGregorianDateTime");
            return "";
        }

    }

    public static String getShamsiDate() {
        try {
            PersianCalendar pc = new PersianCalendar();
            AppMonitor.Log("Year:" + pc.get(Calendar.YEAR));
            AppMonitor.Log("Month:" + pc.get(Calendar.MONTH) + 1);
            AppMonitor.Log("Day:" + pc.get(Calendar.DAY_OF_MONTH));
            return pc.get(Calendar.YEAR) + "/" + (pc.get(Calendar.MONTH) + 1) + "/" + pc.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            Log.e("DateHelper", "getShamsiDate");
            return "";
        }
    }

}