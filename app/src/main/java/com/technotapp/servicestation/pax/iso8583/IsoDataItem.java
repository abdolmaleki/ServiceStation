package com.technotapp.servicestation.pax.iso8583;

class IsoDataItem {
    private String value;
    private int element;
    IsoDataItem(int element, String value){
        this.element=element;
        this.value=value;
    }

    int getElement() {
        return element;
    }

    public String getValue() {
        return value;
    }
}

