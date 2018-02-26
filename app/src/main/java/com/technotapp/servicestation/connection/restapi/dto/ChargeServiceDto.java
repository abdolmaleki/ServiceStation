package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.Date;

@DontObfuscate
public class ChargeServiceDto extends BaseDto {

    public String tokenId;
    public String terminalCode;
    public TransactionParametrModel transactionModel = new TransactionParametrModel();//مدل ثبت تراکنش
    public ChargeParameterModel chargeModel = new ChargeParameterModel();//مدل خرید شارژ تراکنش

    @DontObfuscate
    public class TransactionParametrModel implements Serializable {
        public long deviceTransactionId;//کد تراکنش ایجاد شده در موبایل
        public Date transactionDateTime;//زمان ثبت تراکنش
        public String cardNumber;//شماره کارت مشتری در صورت وجود
        public long accountNumber;//شماره حساب مشتری
        public long idMerchant;
        public byte[] accountPin; // رمز حساب


    }

    @DontObfuscate
    public class ChargeParameterModel implements Serializable {
        public String mobileNumber;
        public int amount;
        public String provider;
        //1- ایرانسل
        //2-همراه اول
        //3- رایتل
        public int service;
        //1- شارژ اعتباری
        //2- شارژ دایمی
        //4- وایمکس
        //5- بسته اینترنتی
        public String param; //برای ارسال شگفت انگیز در ایرانسل 4 و در رایتل 3 و در غیر اینصورت 0 ارسال گردد
        public String userOrderId; //کد سفارش باید یک مقدار منحصر بفرد ارسال گردد
        //در نظر داشته باشید شارژ شورانگیز رایتل برای مبالغ 20000 و50000 و100000 و200000و500000و1000000 ریالی در نظر گرفته شده است و سایر مبالغ معتبر نمیباشد و با خطا مواجه خواهد شد
        //در نظر داشته باشید شارژ شگفت انگیز ایرانسل برای مبالغ 10000،20000،50000،100000،200000 ریالی در نظر گرفته شده است و سایر مبالغ معتبر نمیباشد و با خطا مواجه خواهد شد
        //در نظر داشته باشید شارژ همراه اول فقط برای مبالغ 10000،20000،50000،100000،200000 ریالی در نظر گرفته شده است و سایر مبالغ معتبر نمیباشد و با خطا مواجه خواهد شد
    }

}
