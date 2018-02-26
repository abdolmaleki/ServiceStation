package com.technotapp.servicestation.Infrastructure;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class Converters {

    private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String hexToString(String hexString) {
        try {
            int len = hexString.length();
            byte[] data = new byte[len / 2];

            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
            }

            String result;

            result = new String(data, "utf-8");
            return result;
        } catch (Exception e) {
            Log.e("Converters", "hexToString");
            return null;
        }


    }

    public static String convertEnDigitToPersian(String str) {
        str = str.replace("1", "١").replace("2", "٢").replace("3", "٣").replace("4", "٤").replace("5", "٥").replace("6", "٦").replace("7", "٧").replace("8", "٨").replace("9", "٩").replace("0", "٠");
        return str;
    }

    public static String panNumbrToStar(String str) {
        if(str.length()>=16) {
            return str.substring(12) + "-**-" + str.substring(0, 7);
        }else{
            return str.substring(8) + "-**-" + str.substring(0, 5);

        }
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

    public static String bytesToHex(byte[] bytes) {
        try {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        } catch (Exception e) {
            Log.e("Converters", "bytesToHex");
            return null;
        }
    }

    public static String hexToBin(String hex) {
        try {
            StringBuilder bin = new StringBuilder();
            String binFragment;
            int iHex;
            hex = hex.trim();
            hex = hex.replaceFirst("0x", "");

            for (int i = 0; i < hex.length(); i++) {
                iHex = Integer.parseInt("" + hex.charAt(i), 16);
                binFragment = Integer.toBinaryString(iHex);

                while (binFragment.length() < 4) {
                    binFragment = "0".concat(binFragment);
                }
                bin.append(binFragment);
            }
            return bin.toString();
        } catch (Exception e) {
            Log.e("Converters", "hexToBin");
            return null;
        }
    }

}
