package com.technotapp.servicestation.connection.socket;

import com.technotapp.servicestation.adapter.DataModel.TransactionDataModel;

public interface ISocketCallback {

    void onFail();

    void onReceiveData(TransactionDataModel transactionDataModel);
}
