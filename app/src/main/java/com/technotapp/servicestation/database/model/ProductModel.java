package com.technotapp.servicestation.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductModel extends RealmObject {

    @PrimaryKey
    public int id;
    public String tokenId;
    public String terminalCode;
    public long nidProduct; // only for update
    public int unitCode;
    public String title;
    public String price;
    public String description;

    public void setId(int id) {
        this.id = id;
    }
}
