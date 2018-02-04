package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.util.Date;

@DontObfuscate
public class TransactionArchiveDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public long idSeller;
    public Date dateFrom;
    public Date dateTo;
    public int skipRows;
    public int takeRows;

}
