package com.technotapp.servicestation.mapper;


import android.util.Log;

import com.technotapp.servicestation.Infrastructure.DateHelper;
import com.technotapp.servicestation.adapter.DataModel.ArchiveTransactionAdapterModel;
import com.technotapp.servicestation.connection.restapi.sto.SearchTransactionSto;

import java.util.ArrayList;
import java.util.List;

public class TransactionMapper {
    public static ArrayList<ArchiveTransactionAdapterModel> convertStoToAdaptrerModel(List<SearchTransactionSto.DataModel.Result> results) {
        try {
            ArrayList<ArchiveTransactionAdapterModel> models = new ArrayList<>();
            for (SearchTransactionSto.DataModel.Result result : results) {
                ArchiveTransactionAdapterModel model = new ArchiveTransactionAdapterModel();
                model.amount = result.amount;
                model.accountNumber = result.accountNumber;
                model.cardNumber = (result.cardNumber == null) ? String.valueOf(result.accountNumber) : result.cardNumber;
                model.date = DateHelper.miladiToShamsiِDate(result.dateTime);
                model.time = DateHelper.miladiToShamsiِTime(result.dateTime);
                model.description = result.description;
                model.transactionId = result.deviceTransactionID;
                model.transactionTypeTitle = result.transactionTypeTitle;

                models.add(model);
            }

            return models;
        } catch (Exception e) {
            Log.e("Erroooooor-->", e.getMessage());
            return null;
        }

    }
}
