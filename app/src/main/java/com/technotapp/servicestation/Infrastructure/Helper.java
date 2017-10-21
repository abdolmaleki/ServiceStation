package com.technotapp.servicestation.Infrastructure;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class Helper {
    public static String convertEnDigitToPersian(String str) {
        str = str.replace("1", "١").replace("2", "٢").replace("3", "٣").replace("4", "٤").replace("5", "٥").replace("6", "٦").replace("7", "٧").replace("8", "٨").replace("9", "٩").replace("0", "٠");
        return str;
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
}
