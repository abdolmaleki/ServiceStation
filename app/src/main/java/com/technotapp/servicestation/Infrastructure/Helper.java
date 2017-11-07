package com.technotapp.servicestation.Infrastructure;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.desai.vatsal.mydynamictoast.MyCustomToast;
import com.google.gson.reflect.TypeToken;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.application.Constant;
import com.technotapp.servicestation.connection.restapi.sto.BaseSto;
import com.technotapp.servicestation.connection.restapi.sto.MenuSto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class Helper {
    public static String convertEnDigitToPersian(String str) {
        str = str.replace("1", "١").replace("2", "٢").replace("3", "٣").replace("4", "٤").replace("5", "٥").replace("6", "٦").replace("7", "٧").replace("8", "٨").replace("9", "٩").replace("0", "٠");
        return str;
    }

    public static String getDeviceInfo() {

        return android.os.Build.MODEL;

    }


    public static Bitmap ConvertTextToHighlitedText(String text, float size) {

        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(size);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.BLACK);
        canvas.drawText(text, 0, baseline, paint);
        return image;
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
}
