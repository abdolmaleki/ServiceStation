package com.technotapp.servicestation.pax.printer.printcontent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pax.gl.IGL;
import com.pax.gl.imgprocessing.IImgProcessing;
import com.pax.ippi.impl.Neptune;
import com.pax.ippi.impl.NeptuneUser;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.pax.printer.Printable;

import static com.technotapp.servicestation.application.Constant.Pax.Printer.FONT_NORMAL;
import static com.technotapp.servicestation.application.Constant.Pax.Printer.FONT_SMALL;
import static com.technotapp.servicestation.pax.printer.PrinterHelper.textToBitmap;

public class TransferContent extends Printable {

    @Override
    public Bitmap getContent(Context ctx, String... contents) {
        IGL gl;

        Neptune service = NeptuneUser.getInstance(ctx).getService();
        gl = service.getGl();
        IImgProcessing img = gl.getImgProcessing();
        IImgProcessing.IPage page = img.createPage();

        page.addLine().addUnit(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_technotapp), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit((contents[0]), FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER); //seller
        page.addLine();
        page.addLine().addUnit(Helper.convertEnDigitToPersian(contents[1]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("تلفن فروشگاه"), IImgProcessing.IPage.EAlign.RIGHT); //tell
        page.addLine().addUnit(Helper.convertEnDigitToPersian(contents[2]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("گد پستی"), IImgProcessing.IPage.EAlign.RIGHT); // postal code
        page.addLine();
        page.addLine().addUnit(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_dash_line), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Helper.ConvertTextToHighlitedText("رسید انتقال وجه", 24.0f), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Helper.convertEnDigitToPersian(contents[3]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(Helper.convertEnDigitToPersian(contents[4]), FONT_SMALL, IImgProcessing.IPage.EAlign.RIGHT); //time - date
        page.addLine().addUnit("مبلغ " + Helper.convertEnDigitToPersian(String.format("%,d", Long.parseLong(contents[8]))) + " ریال", FONT_SMALL, IImgProcessing.IPage.EAlign.CENTER); //amount
        page.addLine().addUnit(Helper.convertEnDigitToPersian(contents[5]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("بانک مبدا: "), IImgProcessing.IPage.EAlign.RIGHT); //ref no
        page.addLine().addUnit(Helper.convertEnDigitToPersian(contents[6]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("از کارت شماره: "), IImgProcessing.IPage.EAlign.RIGHT); //terminal no
        page.addLine().addUnit(Helper.convertEnDigitToPersian(contents[5]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("بانک مقصد: "), IImgProcessing.IPage.EAlign.RIGHT); //ref no
        page.addLine().addUnit(Helper.convertEnDigitToPersian(contents[6]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("به کارت شماره: "), IImgProcessing.IPage.EAlign.RIGHT); //terminal no
        page.addLine();
        page.addLine().addUnit(" ", FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit("به نام سجاد عبدالملکی انتقال یافت", FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_dash_line), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Helper.ConvertTextToHighlitedText("www.TechnoTap.com", 24.0f), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Helper.convertEnDigitToPersian("مرکز تماس: 1505 و 02122578583"), FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER);
        Bitmap prnbmp = img.pageToBitmap(page, 384);

        return prnbmp;
    }
}
