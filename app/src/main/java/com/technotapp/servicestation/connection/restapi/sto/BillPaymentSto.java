package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;

@DontObfuscate
public class BillPaymentSto extends BaseTransactionSto {
    public BillPaymentResult billResult;

    @DontObfuscate
    public class BillPaymentResult implements Serializable {
        public String errorCode;//متن نتیجه
        public String errorString;//کد نتیجه
        public long nidBill;//شناسه قبض در سیستم
    }
}
