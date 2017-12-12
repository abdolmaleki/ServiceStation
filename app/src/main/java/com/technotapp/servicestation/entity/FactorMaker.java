package com.technotapp.servicestation.entity;


import com.technotapp.servicestation.adapter.DataModel.ProductFactorAdapterModel;

import java.util.HashMap;
import java.util.Map;

public class FactorMaker {

    private static Map<Integer, ProductFactorAdapterModel> factorMap = new HashMap<Integer, ProductFactorAdapterModel>();

    public long getTotalPrice() {
        long totalPrice = 0;
        for (ProductFactorAdapterModel model : factorMap.values()) {
            totalPrice += model.getAmount() * Long.parseLong(model.getUnitPrice());
        }

        return totalPrice;
    }

    public void updateFactor(ProductFactorAdapterModel model) {
        if (model.getAmount() == 0) {
            factorMap.remove(model.getId());
        } else {
            factorMap.put(model.getId(), model);
        }

    }


}
