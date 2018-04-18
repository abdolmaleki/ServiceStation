package com.technotapp.servicestation.Infrastructure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.entity.MyProgressDialog;
import com.technotapp.servicestation.fragment.AlertDialogFragment;

import java.util.Date;
import java.util.Locale;

public class Helper {


    private static MyProgressDialog progressDialog;

    public static String getDeviceInfo() {
        return android.os.Build.MODEL;
    }

    public static class progressBar {

        public static void showDialog(Context activity, String message) {

            progressDialog = new MyProgressDialog(activity);
            progressDialog.show();
            progressDialog.setMessageText(message);
        }

        public static void hideDialog() {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public static void alert(Context activity, String message, int alertType) {
        AlertDialogFragment dialogFragment = AlertDialogFragment.newInstance(alertType, message);
        dialogFragment.show((Activity) activity);
    }

    public static boolean IsAppUpToDate() {
        boolean isUptodate = false;
        return isUptodate;
    }

    public static void lunchActivity(Context ctx, String controllerName, int menuId) {
        final String PACKAGE_NAME = ctx.getApplicationContext().getPackageName() + ".activity";
        String activityToStart = PACKAGE_NAME + "." + controllerName;
        try {
            Class<?> destActivity = Class.forName(activityToStart);
            Intent intent = new Intent(ctx, destActivity);
            intent.putExtra(Constant.Key.MENU_ID, menuId);
            ctx.startActivity(intent);
        } catch (ClassNotFoundException ignored) {
            Log.e("Helper", "lunchActivity");
        }

    }

    public static Drawable getDrawable(Context ctx, String name) {
        try {
            Resources resources = ctx.getResources();
            final int resourceId = resources.getIdentifier(name, "drawable",
                    ctx.getPackageName());
            return resources.getDrawable(resourceId);
        } catch (Exception e) {
            Log.e("Helper", "getDrawable");
            return null;
        }

    }

    public static int getImageOrganization(byte organizationCode) {
        switch (organizationCode) {
            case Constant.PayBill.Organization.WATER:
                return R.drawable.ic_organization_water;
            case Constant.PayBill.Organization.ELECTRICAL:
                return R.drawable.ic_organization_electrical;
            case Constant.PayBill.Organization.GAS:
                return R.drawable.ic_organization_gas;
            case Constant.PayBill.Organization.PHONE:
                return R.drawable.ic_organization_phone;
            case Constant.PayBill.Organization.MOBILE:
                return R.drawable.ic_organization_phone;
            case Constant.PayBill.Organization.TAX_OF_MUNICIPALITY:
                return R.drawable.ic_organization_tax_of_municipality;
            case Constant.PayBill.Organization.TAX:
                return R.drawable.ic_organization_tax;
            case Constant.PayBill.Organization.TRAFFIC_CRIME:
                return R.drawable.ic_organization_traffic_police;
            default:
                return 0;
        }
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Date.class, deser)
                .create();
    }

    static JsonDeserializer<Date> deser = (json, typeOfT, context) -> json == null ? null : new Date(json.getAsLong());

    public static String getBankName(String panNumber) {

        switch (panNumber) {
            case "627412":
                return "اقتصاد نوین";
            case "627381":
                return "انصار";
            case "505785":
                return "ایران زمین";
            case "622106":
            case "639194":
            case "627884":
                return "پارسیان";
            case "639347":
            case "502229":
                return "پاسارگاد";
            case "636214":
                return "آینده";
            case "627353":
                return "تجارت";
            case "502908":
                return "توسعه تعاون";
            case "627648":
            case "207177":
                return "توسعه صادرات ایران";
            case "636949":
                return "حکمت ایرانیان";
            case "502938":
                return "دی";
            case "589463":
                return "رفاه کارگران";
            case "621986":
                return "سامان";
            case "589210":
                return "سپه";
            case "639607":
                return "سرمایه";
            case "639346":
                return "سینا";
            case "502806":
                return "شهر";
            case "603769":
                return "صادرات ایران";
            case "627961":
                return "صنعت و معدن";
            case "606373":
                return "قرض الحسنه مهر ایران";
            case "639599":
                return "قوامین";
            case "627488":
            case "502910":
                return "کارآفرین";
            case "603770":
            case "639217":
                return "کشاورزی";
            case "505416":
                return "گردشگری";
            case "636795":
                return "مرکزی";
            case "628023":
                return "مسکن";
            case "610433":
            case "991975":
                return "ملت";
            case "603799":
                return "ملی ایران";
            case "639370":
                return "مهر اقتصاد";
            case "627760":
                return "پست بانک ایران";
            case "628157":
                return "موسسه اعتباری توسعه";
            case "505801":
                return "موسسه اعتباری کوثر";
            default:
                return "بانک نامشخص";

        }

    }

    public static String getAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = null;
            try {
                info = manager.getPackageInfo(
                        context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return info.versionName;
        } catch (Exception e) {
            return "0.0.0";

        }

    }
}
