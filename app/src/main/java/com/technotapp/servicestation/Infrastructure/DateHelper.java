package com.technotapp.servicestation.Infrastructure;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.PersianCalendar;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.technotapp.servicestation.application.Constant;

import java.text.DateFormat;
import java.text.ParseException;
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

    public static Date shamsiToMiladiDate(PersianCalendar persianCalendar) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(persianCalendar.getTime());
        return gregorianCalendar.getTime();

    }

    public static String miladiToShamsiِDate(String miladi) {
        try {
            DateFormat originDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = originDate.parse(miladi);
            PersianCalendar persianCalendar = new PersianCalendar(date);
            return persianCalendar.get(Calendar.YEAR) + "/" + persianCalendar.get(Calendar.MONTH) + "/" + persianCalendar.get(Calendar.DAY_OF_MONTH);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String miladiToShamsiِTime(String miladi) {
        try {
            DateFormat originDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = originDate.parse(miladi);
            PersianCalendar persianCalendar = new PersianCalendar(date);
            return persianCalendar.get(Calendar.HOUR_OF_DAY) + ":" + (persianCalendar.get(Calendar.MINUTE) > 9 ? persianCalendar.get(Calendar.MINUTE) : ("0" + persianCalendar.get(Calendar.MINUTE)))
                    ;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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

    public static int showDatePicker(Context context, View view) {
        try {
            DatePickerDialog.OnDateSetListener onDateSetListener = null;
            if (context instanceof DatePickerDialog.OnDateSetListener) {
                onDateSetListener = (DatePickerDialog.OnDateSetListener) context;
            }
            com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar persianCalendar = new com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    onDateSetListener,
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(((Activity) context).getFragmentManager(), "Datepickerdialog");
            return view.getId();
        } catch (Exception e) {
            Helper.alert(context, "خطا در نمایش تقویم", Constant.AlertType.Error);
            AppMonitor.reportBug(context, e, "DateHelper", "showDatePicker");
            return 0;

        }
    }

    public static long differenceDateInDay(long oldDateMilisecond, long newDateMilisecond) {
        try {
            long diff = newDateMilisecond - oldDateMilisecond;
            return diff / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return 0;
        }

    }

}