package com.technotapp.servicestation.connection.restapi.dto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.Date;

@DontObfuscate
public class TerminalTransactionDto extends BaseDto {

    public String tokenId;
    public String terminalCode;
    public TransactionParametr transactionModel;

    @DontObfuscate
    public class TransactionParametr implements Serializable {
        public long deviceTransactionId;
        public long idMerchant;
        public String codeTerminal;
        public String codeTerminalType;
        public String codeTransactionType;
        public Date transactionDateTime;
        public String cardNumber;
        public long accountNumber;
        public String accountPin;
        public long amountOfTransaction;
        public String merchantTrace;
        public String mediaSignature;
        public long nidSettlement;
        public boolean isSettle;
        public String gatewayRefId;
        public String bankRefId;
        public String bankFollowCode;
        public String description;
        public int statusId;
    }
}
