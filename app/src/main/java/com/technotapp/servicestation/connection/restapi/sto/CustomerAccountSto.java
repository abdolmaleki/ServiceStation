package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DontObfuscate
public class CustomerAccountSto extends BaseSto {

    public List<DataModel> dataModel = null;

    @DontObfuscate
    public class DataModel implements Serializable {
        public ArrayList<CustomerAccount> accounts = null;
        public ArrayList<Score> scores = null;

        @DontObfuscate
        public class CustomerAccount implements Serializable {

            public long accountNumber;
            public Integer nidAccountType;
            public String title;
            public int isActivePin;

        }

        @DontObfuscate
        public class Score implements Serializable {

            public long score;
            public long scoreCoefficient;
            public long scorePrice;

        }
    }
}
