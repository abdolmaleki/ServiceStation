package com.technotapp.servicestation.connection.restapi.dto;

import java.util.Date;

public class TransactionArchiveDto extends BaseDto {
    public String tokenId;
    public String terminalCode;
    public long idSeller;
    public Date dateFrom;
    public Date dateTo;
    public int skipRows;
    public int takeRows;

}
