package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class LogDto extends BaseDto {

    public String deviceIp;
    public String tokenId;
    public String terminalCode;
    public Integer LogTypeId;
    public String Title;
    public String Description;
    public String UserDeviceInfo;
}
