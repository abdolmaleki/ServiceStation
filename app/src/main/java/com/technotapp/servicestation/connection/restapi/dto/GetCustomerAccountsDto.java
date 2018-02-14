package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class GetCustomerAccountsDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public String cardNumber;

}
