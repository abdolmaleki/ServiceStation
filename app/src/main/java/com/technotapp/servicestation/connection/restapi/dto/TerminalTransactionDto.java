package com.technotapp.servicestation.connection.restapi.dto;

import com.google.gson.annotations.Expose;
import com.technotapp.servicestation.Infrastructure.DontObfuscate;
import com.technotapp.servicestation.enums.PaymentType;

import java.io.Serializable;
import java.util.Date;

@DontObfuscate
public class TerminalTransactionDto extends BaseDto {

    public String tokenId;
    public String terminalCode;
    public TransactionParametrModel transactionModel = new TransactionParametrModel();
    @Expose
    public String paymentType;

}
