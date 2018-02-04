package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class SuggestionDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public String title;
    public String description;
    public String userDeviceInfo;
    public String deviceIp;
}
