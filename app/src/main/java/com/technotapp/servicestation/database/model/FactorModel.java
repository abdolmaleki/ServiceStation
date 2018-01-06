package com.technotapp.servicestation.database.model;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;

import io.realm.RealmList;
import io.realm.RealmObject;

@DontObfuscate
public class FactorModel extends RealmObject {

    private long id;
    private RealmList<ProductFactorAdapterModel> productModels;
    private long totalPrice;
    private String date;
    private boolean isPaid;

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setProductModels(RealmList<ProductFactorAdapterModel> productFactorAdapterModels) {
        this.productModels = productFactorAdapterModels;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public long getId() {
        return id;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public String getDate() {
        return date;
    }

    public RealmList<ProductFactorAdapterModel> getProductModels() {
        return productModels;
    }

    public boolean isPaid() {
        return isPaid;
    }
}
