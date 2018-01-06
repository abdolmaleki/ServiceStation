package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class ChargeSto extends BaseSto {

    public String resultCode; //کد نتیجه اجرای متد
    public String note; //تشریخ فارسی نتیجه اجرا متد
    public String transactionID; //کد رهگیری تراکنش
    public String amount; //مبلغ تراکنش
    public String mobileNumber; //شماره همراه
    public String transactionTime; //زمان انجام تراکنش
    public String creadit; //  مانده اعتبار کاربر
}
