package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

@DontObfuscate
public class GetCustomerAccountsVerificationDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public String cardNumber;
    public String idHashCustomer;
    public long nidTransaction;

}
