package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class GetInternetPackageDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public int operatorId;//کد اپراتور
    //1- ایرانسل
    //2-همراه اول
    //3- رایتل
}
