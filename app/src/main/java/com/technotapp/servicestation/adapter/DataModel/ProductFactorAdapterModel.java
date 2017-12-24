package com.technotapp.servicestation.adapter.DataModel;

import io.realm.Realm;
import io.realm.RealmObject;

public class ProductFactorAdapterModel extends RealmObject {
    private long nidProduct;
    private String unitPrice;
    private String unit;
    private String name;
    private String description;
    private String sumPrice;
    public int amount = 0;

    public long getNidProduct() {
        return nidProduct;
    }

    public void setnidProduct(long id) {
        this.nidProduct = id;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getSumPrice() {

        Realm realm = getRealm();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                sumPrice = String.valueOf(amount * Long.parseLong(unitPrice));
            }
        });
        return sumPrice;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
