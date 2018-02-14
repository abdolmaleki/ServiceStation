package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;

@DontObfuscate
public class BillPaymentSto extends BaseSto {
    public String errorCode;
    public String errorString;
    public long nidTransaction;
    public long deviceTransactionID;
    public BillPaymentResult billResult;

    @DontObfuscate
    private class BillPaymentResult implements Serializable {
        public String errorCode;//متن نتیجه
        public String errorString;//کد نتیجه
        public long nidBill;//شناسه قبض در سیستم
    }
}
