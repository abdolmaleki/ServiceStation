package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.Date;

@DontObfuscate
public class BillPaymentDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public TransactionParametrModel transactionModel = new TransactionParametrModel();
    public BillParameter billModel = new BillParameter();


    @DontObfuscate
    public class BillParameter implements Serializable {
        public String userOrderId;//شناسه تراکنش در پوز
        public long amount;//مبلغ قبض
        public String billingId;//شناسه قبض
        public String paymentId;//شناسه پرداخت
        public String description;
    }

}
