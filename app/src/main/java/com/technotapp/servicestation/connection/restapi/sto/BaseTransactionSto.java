package com.technotapp.servicestation.connection.restapi.sto;

import com.google.gson.annotations.Expose;
import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class BaseTransactionSto {
    public String newMacKey;
    public String newPinKey;
    public String errorCode;
    public String errorString;
    public long nidTransaction;
    public long deviceTransactionID;
    public long accountAmount;

    @Expose
    public String paymentType;
    @Expose
    public String cardNumber;
    @Expose
    public String amount;

}
