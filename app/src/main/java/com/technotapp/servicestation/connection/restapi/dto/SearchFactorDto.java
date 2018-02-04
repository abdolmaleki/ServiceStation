package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.util.Date;

@DontObfuscate
public class SearchFactorDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public Long nidFactor = null;
    public long idSeller;
    public Date dateFrom;
    public Date dateTo;
    public int skipRows;
    public int takeRows;
}
