package com.technotapp.servicestation.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductModel extends RealmObject {

    @PrimaryKey
    public long nidProduct;
    public String tokenId;
    public String terminalCode;
    public int unitCode;
    public String title;
    public String price;
    public String description;

    public void setId(long nidProduct) {
        this.nidProduct = nidProduct;
    }
}
