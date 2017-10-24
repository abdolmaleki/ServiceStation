package com.technotapp.servicestation.Infrastructure;

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
            AppMonitor.reportBug(e, "DateHelper", "getGregorianDateTime");
            return "";
        }

    }
}
