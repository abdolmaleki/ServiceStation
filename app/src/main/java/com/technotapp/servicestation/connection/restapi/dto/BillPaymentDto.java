package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.Date;

@DontObfuscate
public class BillPaymentDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public TransactionParametr transactionModel = new TransactionParametr();
    public BillParameter billModel = new BillParameter();

    @DontObfuscate
    public class TransactionParametr implements Serializable {
        public long deviceTransactionId;//کد تراکنش ایجاد شده در موبایل
        public Date transactionDateTime;//زمان ثبت تراکنش
        public String cardNumber;//شماره کارت مشتری در صورت وجود
        public long accountNumber;//شماره حساب مشتری
        public byte[] accountPin;
        public long idMerchant;
    }

    @DontObfuscate
    public class BillParameter implements Serializable {
        public String userOrderId;//شناسه تراکنش در پوز
        public long amount;//مبلغ قبض
        public String billingId;//شناسه قبض
        public String paymentId;//شناسه پرداخت
        public String description;
    }

}
