package com.technotapp.servicestation.pax.iso8583;

/**
 * Created by Kourosh on 02/10/2017.
 */
public class IsoDataItem {
    String value;
    int element;
    public IsoDataItem(int element, String value){
        this.element=element;
        this.value=value;
    }

    public int getElement() {
        return element;
    }

    public String getValue() {
        return value;
    }
}

