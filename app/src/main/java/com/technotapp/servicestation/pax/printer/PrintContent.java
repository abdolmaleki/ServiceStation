package com.technotapp.servicestation.pax.printer;


import android.graphics.Bitmap;

public abstract class PrintContent {
    Bitmap printBitmap;

    protected abstract Bitmap getContent();

    ///////////////////////////////////////////////////////
    /////////////////// Content Types//////////////////////
    ///////////////////////////////////////////////////////

    protected static final String BALANCE = "balance";
}
