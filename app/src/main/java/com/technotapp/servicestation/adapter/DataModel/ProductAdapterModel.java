package com.technotapp.servicestation.adapter.DataModel;

public class ProductAdapterModel {
    private long nidProduct;
    private String price;
    private String unit;
    private String name;
    private String description;

    public long getId() {
        return nidProduct;
    }

    public void setId(long nidProduct) {
        this.nidProduct = nidProduct;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
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
