package com.technotapp.servicestation.connection.restapi.sto;

import com.google.gson.annotations.SerializedName;
import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@DontObfuscate
public class SearchTransactionSto extends BaseSto {
    public List<DataModel> dataModel = null;

    @DontObfuscate
    public class DataModel implements Serializable {

        public List<DataRecord> dataRecord = null;
        public List<Result> result = null;

        @DontObfuscate
        public class DataRecord implements Serializable {

            @SerializedName("totalRows")
            public int totalRows;
            @SerializedName("skipRows")
            public int skipRows;
            @SerializedName("takeRows")
            public int takeRows;

        }

        @DontObfuscate
        public class Result implements Serializable {

            public long deviceTransactionID;
            public String eNidTransaction;
            public String codeTerminal;
            public String transactionTypeTitle;
            public String dateTime;
            public String cardNumber;
            public long accountNumber;
            public long amount;
            public String description;

        }

    }

}
