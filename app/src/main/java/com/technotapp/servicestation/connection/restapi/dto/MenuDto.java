package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class MenuDto extends BaseDto {

    public String userName;
    public String password;
    public String terminalCode;
    public String deviceInfo;
}
