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
    public static final String BALANCE_NO_ICON = "balance.no.icon";

    public static final String DEPOSIT = "deposit";
    public static final String DEPOSIT_NO_ICON = "deposit.no.icon";

    public static final String BUY_SELLER = "seller";
    public static final String BUY_SELLER_NO_ICON = "seller.no.icon";

    public static final String BUY_CUSTOMER = "customer";
    public static final String BUY_CUSTOMER_NO_ICON = "customer.no.icon";

    public static final String CASH = "CASH";
    public static final String CASH_NO_ICON = "CASH.no.icon";

}
