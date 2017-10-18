package com.technotapp.servicestation.pax.iso8583;

import java.util.ArrayList;

/**
 * Created by Kourosh on 02/10/2017.
 */

public class IsoItem {
    ArrayList<IsoDataItem> isoDataItemArray ;
    String length , mti,bitmap;
    public IsoItem(String length, String mti, String bitmap, ArrayList<IsoDataItem> isoDataItemArray){
        this.isoDataItemArray=isoDataItemArray;
        this.length=length;
        this.mti=mti;
        this.bitmap=bitmap;
    }

    public ArrayList<IsoDataItem> getIsoDataItemArray() {
        return isoDataItemArray;
    }

    public String getBitmap() {
        return bitmap;
    }

    public String getLength() {
        return length;
    }

    public String getMti() {
        return mti;
    }
    public IsoDataItem getNode(int type){

        for(IsoDataItem item:isoDataItemArray)
            if (item.getElement()==type)
                return item;

        return null;
    }
}

