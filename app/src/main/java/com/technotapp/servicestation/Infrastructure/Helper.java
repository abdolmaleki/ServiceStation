package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.desai.vatsal.mydynamictoast.MyCustomToast;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;

import dmax.dialog.SpotsDialog;

public class Helper {


    public static String getDeviceInfo() {

        return android.os.Build.MODEL;

    }

    public static class ProgressBar {

        private static SpotsDialog dialog;

        public static void showDialog(Context ctx, String message) {

            dialog = new SpotsDialog(ctx, message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        public static void hideDialog() {
            dialog.dismiss();
        }
    }

    public static void alert(Context ctx, String message, int alertType, int alertLenght) {
        MyCustomToast myCustomToast = new MyCustomToast(ctx);
        myCustomToast.setCustomMessageText(message);
        myCustomToast.setCustomMessageTextColor(Color.WHITE);
        myCustomToast.setCustomMessageIconColor(Color.WHITE);
        myCustomToast.setCustomMessageDuration(alertLenght);

        if (alertType == Constant.AlertType.Information) {
            myCustomToast.setCustomMessageIcon(R.drawable.ic_info, MyCustomToast.POSITION_RIGHT);
            myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.info_message_background);
        } else if (alertType == Constant.AlertType.Warning) {
            myCustomToast.setCustomMessageIcon(R.drawable.ic_warning, MyCustomToast.POSITION_RIGHT);
            myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.warning_message_background);
        } else if (alertType == Constant.AlertType.Error) {
            myCustomToast.setCustomMessageIcon(R.drawable.ic_error, MyCustomToast.POSITION_RIGHT);
            myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.error_message_background);
        } else if (alertType == Constant.AlertType.Done) {
            myCustomToast.setCustomMessageIcon(R.drawable.ic_success, MyCustomToast.POSITION_RIGHT);
            myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.success_message_background);
        }

        myCustomToast.show();
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
            AppMonitor.reportBug(ignored, "Helper", "lunchActivity");
        }

    }

    public static Drawable getDrawable(Context ctx, String name) {
        try {
            Resources resources = ctx.getResources();
            final int resourceId = resources.getIdentifier(name, "drawable",
                    ctx.getPackageName());
            return resources.getDrawable(resourceId);
        } catch (Exception e) {
            AppMonitor.reportBug(e, "Helper", "getDrawable");
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
                .create();
    }
}
