package com.technotapp.servicestation.entity;

import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;
import com.technotapp.servicestation.database.model.FactorModel;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;

public class FactorMaker {

    private Map<Long, ProductFactorAdapterModel> factorMap = new HashMap<Long, ProductFactorAdapterModel>();

    public long getTotalPrice() {
        long totalPrice = 0;
        for (ProductFactorAdapterModel model : factorMap.values()) {
            totalPrice += model.getAmount() * Long.parseLong(model.getUnitPrice());
        }

        return totalPrice;
    }

    public void updateFactor(ProductFactorAdapterModel model) {
        if (model.getAmount() == 0) {
            factorMap.remove(model.getNidProduct());
        } else {
            factorMap.put(model.getNidProduct(), model);
        }

    }

    public FactorModel exportFactor() {
        FactorModel factorModel = new FactorModel();
        RealmList<ProductFactorAdapterModel> productList = new RealmList<>();
        for (Map.Entry<Long, ProductFactorAdapterModel> obj : factorMap.entrySet()) {
            ProductFactorAdapterModel adapterModel = obj.getValue();
            productList.add(adapterModel);
        }
        factorModel.setProductModels(productList);
        factorModel.setTotalPrice(getTotalPrice());
        factorModel.setDate(DateHelper.getShamsiDate());
        factorModel.setPaid(false);

        return factorModel;
    }
}
