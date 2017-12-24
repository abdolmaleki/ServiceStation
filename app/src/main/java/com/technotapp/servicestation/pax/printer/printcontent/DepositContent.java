package com.technotapp.servicestation.pax.printer.printcontent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pax.gl.IGL;
import com.pax.gl.imgprocessing.IImgProcessing;
import com.pax.ippi.impl.Neptune;
import com.pax.ippi.impl.NeptuneUser;
import com.technotapp.servicestation.Infrastructure.Converters;
import com.technotapp.servicestation.Infrastructure.Helper;
import com.technotapp.servicestation.R;
import com.technotapp.servicestation.pax.printer.Printable;

import static com.technotapp.servicestation.application.Constant.Pax.Printer.FONT_NORMAL;
import static com.technotapp.servicestation.application.Constant.Pax.Printer.FONT_SMALL;
import static com.technotapp.servicestation.pax.printer.PrinterHelper.textToBitmap;


public class DepositContent extends Printable {
    @Override
    public Bitmap getContent(Context ctx, String... contents) {
        IGL gl;

        Neptune service = NeptuneUser.getInstance(ctx).getService();
        gl = service.getGl();
        IImgProcessing img = gl.getImgProcessing();
        IImgProcessing.IPage page = img.createPage();

        page.addLine().addUnit(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_technotapp_print), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit((contents[0]), FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER); //seller
        page.addLine();
        page.addLine().addUnit(Converters.convertEnDigitToPersian(contents[1]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("تلفن فروشگاه"), IImgProcessing.IPage.EAlign.RIGHT); //tell
        page.addLine().addUnit(Converters.convertEnDigitToPersian(contents[2]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("گد پستی"), IImgProcessing.IPage.EAlign.RIGHT); // postal code
        page.addLine();
        page.addLine().addUnit(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_dash_line), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Converters.ConvertTextToHighlitedText("واریز به حساب", 24.0f), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Converters.convertEnDigitToPersian(contents[6]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("پذیرنده/پایانه"), IImgProcessing.IPage.EAlign.RIGHT); // terminal no
        page.addLine().addUnit(Converters.panNumbrToStar(Converters.convertEnDigitToPersian(contents[7])), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap(Helper.getBankName(contents[7].substring(0,6))), IImgProcessing.IPage.EAlign.RIGHT); // card no
        page.addLine().addUnit(Converters.convertEnDigitToPersian(contents[3]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(Converters.convertEnDigitToPersian(contents[4]), FONT_SMALL, IImgProcessing.IPage.EAlign.RIGHT); // time - date
        page.addLine().addUnit(Converters.convertEnDigitToPersian(contents[5]), FONT_SMALL, IImgProcessing.IPage.EAlign.LEFT).addUnit(textToBitmap("پیگیری/ارجاع"), IImgProcessing.IPage.EAlign.RIGHT); // ref no
        page.addLine();
        page.addLine().addUnit(" ", FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit("مبلغ " + Converters.convertEnDigitToPersian(String.format("%,d", Long.parseLong(contents[8]))) + " ریال", FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER); //amount
        page.addLine();
        page.addLine().addUnit("عملیات موفق", FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_dash_line), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Converters.ConvertTextToHighlitedText("www.technotapp.com", 24.0f), IImgProcessing.IPage.EAlign.CENTER);
        page.addLine();
        page.addLine().addUnit(Converters.convertEnDigitToPersian("مرکز تماس: 02122578583"), FONT_NORMAL, IImgProcessing.IPage.EAlign.CENTER);

        return img.pageToBitmap(page, 384);
    }
}
