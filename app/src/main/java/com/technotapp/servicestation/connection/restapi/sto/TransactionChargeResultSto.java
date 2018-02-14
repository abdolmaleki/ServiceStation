package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;

@DontObfuscate
public class TransactionChargeResultSto extends BaseSto {
    public String errorCode;
    public String errorString;
    public int nidTransaction;
    //کد سیستمی تراکنش
    public long deviceTransactionID;
    //کد تراکنشی که شما فرستادین
    public ChargeResult chargeResult;
    //مدل شارژ خریداری شده

    @DontObfuscate
    public class ChargeResult implements Serializable {
        public String resultCode; //کد نتیجه اجرای متد
        public String note;//تشریخ فارسی نتیجه اجرا متد
        public String transactionID; //کد رهگیری تراکنش
        public String amount;//مبلغ تراکنش
        public String mobileNumber;//شماره همراه
        public String transactionTime; //زمان انجام تراکنش
        public String creadit; // مانده اعتبار کاربر
    }
}
