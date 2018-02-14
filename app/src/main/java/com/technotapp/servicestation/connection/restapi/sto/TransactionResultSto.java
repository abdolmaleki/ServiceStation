package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class TransactionResultSto extends BaseSto {

    public String errorCode;
    public String errorString;
    public long nidTransaction;
    public long deviceTransactionID;
    public long accountAmount;
}
