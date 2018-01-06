package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class TerminalInfoDto extends BaseDto {
    public String terminalCode;
    public String tokenId;

}
