package com.technotapp.servicestation.connection.restapi.sto;

import com.google.gson.annotations.SerializedName;
import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.List;

@DontObfuscate
public class SearchFactorSto extends BaseSto {
    public List<DataModel> dataModel = null;

    @DontObfuscate
    public class DataModel implements Serializable {

        public List<DataRecord> dataRecord = null;
        public List<Result> result = null;

        @DontObfuscate
        public class Result implements Serializable {

            public long nidFactor;
            public String terminalCode;
            public String dateTime;
            public long finalPrice;
            public long totalPrice;
            public long discountPrice;
            public boolean status;
            public String statusTitle;

        }

        @DontObfuscate
        public class DataRecord implements Serializable {

            @SerializedName("totalRows")
            public int totalRows;
            @SerializedName("skipRows")
            public int skipRows;
            @SerializedName("takeRows")
            public int takeRows;


        }

    }
}
