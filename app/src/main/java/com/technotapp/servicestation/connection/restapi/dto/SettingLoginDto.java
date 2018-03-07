package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class SettingLoginDto extends BaseDto {

    public String tokenId;
    public String terminalCode;
    public String deviceIP;
    public String userName;
    public String password;

}
