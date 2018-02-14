package com.technotapp.servicestation.mapper;


import com.technotapp.servicestation.adapter.DataModel.PaymentTypeAdapterModel;
import com.technotapp.servicestation.database.model.PaymentModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentMapper {

    public static ArrayList<PaymentTypeAdapterModel> convertPaymentModelToAdapter(List<PaymentModel> models) {
        try {
            ArrayList<PaymentTypeAdapterModel> adapterModels = new ArrayList<>();
            for (PaymentModel model : models) {
                PaymentTypeAdapterModel paymentTypeAdapterModel = new PaymentTypeAdapterModel();
                paymentTypeAdapterModel.code = model.code;
                paymentTypeAdapterModel.title = model.title;
                adapterModels.add(paymentTypeAdapterModel);
            }

            return adapterModels;
        } catch (Exception e) {
            return null;
        }
    }
}
