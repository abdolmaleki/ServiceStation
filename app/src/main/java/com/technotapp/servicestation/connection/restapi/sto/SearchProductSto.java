package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.List;

@DontObfuscate
public class SearchProductSto extends BaseSto {
    public List<DataModel> dataModel;
    @DontObfuscate
    public class DataModel implements Serializable {

        public List<DataRecord> dataRecord = null;
        public List<Result> result = null;

        @DontObfuscate
        public class DataRecord implements Serializable {

            public long totalRows;
            public long skipRows;
            public long takeRows;

        }
        @DontObfuscate
        public class Result implements Serializable {

            public long nidProduct;
            public int unitCode;
            public String unitTitle;
            public long price;
            public String description;
            public String title;

        }
    }
}
