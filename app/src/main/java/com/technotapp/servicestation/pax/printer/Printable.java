package com.technotapp.servicestation.pax.printer;


import android.content.Context;
import android.graphics.Bitmap;

public abstract class Printable {
    Bitmap printBitmap;

    public abstract Bitmap getContent(Context ctx, String... contents);

    ///////////////////////////////////////////////////////
    /////////////////// Content Types//////////////////////
    ///////////////////////////////////////////////////////

    public static final String BALANCE = "balance";
    public static final String DEPOSIT = "deposit";
    public static final String BUY_SELLER = "seller";
    public static final String BUY_CUSTOMER = "customer";
}
