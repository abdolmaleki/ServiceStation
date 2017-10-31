package com.technotapp.servicestation.pax.printer;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;
import com.technotapp.servicestation.Infrastructure.AppMonitor;
import com.technotapp.servicestation.Infrastructure.GetObj;
import com.technotapp.servicestation.Infrastructure.TestLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class PrinterHelper extends TestLog {
    static IDAL idal;

    private static PrinterHelper printerHelper;
    private IPrinter printer;

    private PrinterHelper(IDAL dal) {
        idal = dal;
        printer = GetObj.getDal().getPrinter();
    }

    public static PrinterHelper getInstance() {
        if (printerHelper == null) {
            printerHelper = new PrinterHelper(idal);
        }
        return printerHelper;
    }

    public void init() {
        try {
            printer.init();
            logTrue("init");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("init", e.toString());
        }
    }

    public String getStatus() {
        try {
            int status = printer.getStatus();
            logTrue("getStatus");
            return statusCode2Str(status);
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("getStatus", e.toString());
            return "";
        }

    }

    public void fontSet(EFontTypeAscii asciiFontType, EFontTypeExtCode cFontType) {
        try {
            printer.fontSet(asciiFontType, cFontType);
            logTrue("fontSet");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("fontSet", e.toString());
        }

    }

    public void spaceSet(byte wordSpace, byte lineSpace) {
        try {
            printer.spaceSet(wordSpace, lineSpace);
            logTrue("spaceSet");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("spaceSet", e.toString());
        }
    }

    public void printStr(String str, String charset) {
        try {
            printer.printStr(str, charset);
            logTrue("printStr");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("printStr", e.toString());
        }

    }

    public void step(int b) {
        try {
            printer.step(b);
            logTrue("setStep");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("setStep", e.toString());
        }
    }

    public void printBitmap(Bitmap bitmap) {
        try {
            bitmap.setDensity(300);
            printer.printBitmap(bitmap);
            logTrue("printBitmap");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("printBitmap", e.toString());
        }
    }

    private String start() {
        try {
            int res = printer.start();
            logTrue("start");
            return statusCode2Str(res);
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("start", e.toString());
            return "";
        }

    }

    public void startPrint(final Bitmap bmpPrint) {
        try {


            String printStatus;

            new NewPrint(bmpPrint).execute();
        } catch (Exception e) {
            AppMonitor.reportBug(e, "PrinterHelper", "startPrint");
        }

    }

    public void leftIndents(short indent) {
        try {
            printer.leftIndent(indent);
            logTrue("leftIndent");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("leftIndent", e.toString());
        }
    }

    public int getDotLine() {
        try {
            int dotLine = printer.getDotLine();
            logTrue("getDotLine");
            return dotLine;
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("getDotLine", e.toString());
            return -2;
        }
    }

    public void setGray(int level) {
        try {
            printer.setGray(level);
            logTrue("setGray");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("setGray", e.toString());
        }

    }

    public void setDoubleWidth(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleWidth(isAscDouble, isLocalDouble);
            logTrue("doubleWidth");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("doubleWidth", e.toString());
        }
    }

    public void setDoubleHeight(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleHeight(isAscDouble, isLocalDouble);
            logTrue("doubleHeight");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("doubleHeight", e.toString());
        }

    }

    public void setInvert(boolean isInvert) {
        try {
            printer.invert(isInvert);
            logTrue("setInvert");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("setInvert", e.toString());
        }

    }

    public String cutPaper(int mode) {
        try {
            printer.cutPaper(mode);
            logTrue("cutPaper");
            return "cut paper successful";
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("cutPaper", e.toString());
            return e.toString();
        }
    }

    public String getCutMode() {
        String resultStr = "";
        try {
            int mode = printer.getCutMode();
            logTrue("getCutMode");
            switch (mode) {
                case 0:
                    resultStr = "Only support full paper cut";
                    break;
                case 1:
                    resultStr = "Only support partial paper cutting ";
                    break;
                case 2:
                    resultStr = "support partial paper and full paper cutting ";
                    break;
                case -1:
                    resultStr = "No cutting knife,not support";
                    break;
                default:
                    break;
            }
            return resultStr;
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("getCutMode", e.toString());
            return e.toString();
        }
    }

    public String statusCode2Str(int status) {
        String res = "";
        switch (status) {
            case 0:
                res = "چاپ موفقیت آمیز ";
                break;
            case 1:
                res = "پرینتر مشغول چاپ است ";
                break;
            case 2:
                res = "کاغذ چاپ تمام شده است ";
                break;
            case 3:
                res = "فرمت داده برای چاپ صحیح نیست";
                break;
            case 4:
                res = "پرینتر دچار آسیب شده است ";
                break;
            case 8:
                res = "دمای پرینتر بسیار بالا است ";
                break;
            case 9:
                res = "ولتاز پرینتر بسیار پایین است";
                break;
            case 240:
                res = "عملیات چاپ ناتمام ";
                break;
            case 252:
                res = "کتابخانه فونت مورد نظر نصب نشده است";
                break;
            case 254:
                res = "حجم متن بسیار بالا است ";
                break;
            default:
                break;
        }
        return res;
    }

    public static Bitmap textToBitmap(String text) {
        Paint paint = new Paint(ANTI_ALIAS_FLAG);
        paint.setTextSize(20.0f);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static void copyFontFromAssets(Context ctx) {
        AssetManager assetManager = ctx.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);

                String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fonts/";

                File outFile = new File(outDir, filename);

                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


    private class NewPrint extends AsyncTask<Void, Void, String> {

        Bitmap printBitmap;

        public NewPrint(Bitmap printBitmap) {
            this.printBitmap = printBitmap;
        }

        @Override
        protected String doInBackground(Void... params) {
            getInstance().init();

            ////////////////////////////////////////////////////////////////////////////////////////
            /// set Font //
            ////////////////////////////////////////////////////////////////////////////////////////
            getInstance().fontSet(EFontTypeAscii.FONT_16_16,
                    EFontTypeExtCode.FONT_16_16);

            ////////////////////////////////////////////////////////////////////////////////////////
            /// Print picture //
            ////////////////////////////////////////////////////////////////////////////////////////

            //  PrinterTester.getInstance().printBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_shaparak));

            getInstance().printBitmap(printBitmap);

            ////////////////////////////////////////////////////////////////////////////////////////
            /// Text Styles //
            ////////////////////////////////////////////////////////////////////////////////////////

            getInstance().spaceSet(Byte.parseByte("0"), // word space
                    Byte.parseByte("0")); // line space
            getInstance().leftIndents(Short.parseShort("0")); // left masrgin
            getInstance().setGray(Integer.parseInt("0")); // gray


            ////////////////////////////////////////////////////////////////////////////////////////
            /// Page Size //
            ////////////////////////////////////////////////////////////////////////////////////////

            getInstance().step(Integer.parseInt("135")); // page height

            ////////////////////////////////////////////////////////////////////////////////////////
            /// start print and show status //
            ////////////////////////////////////////////////////////////////////////////////////////

            final String status = getInstance().start();
            return status;
        }

        @Override
        protected void onPostExecute(String printStatus) {
            super.onPostExecute(printStatus);
            AppMonitor.Log(printStatus);

        }
    }


}
