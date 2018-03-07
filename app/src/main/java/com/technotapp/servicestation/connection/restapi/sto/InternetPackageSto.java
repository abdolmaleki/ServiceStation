package com.technotapp.servicestation.connection.restapi.sto;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.List;

@DontObfuscate
public class InternetPackageSto {
    public String errorCode;
    public String errorString;
    public List<InternetPackListResult> internetPackListResult = null;

    @DontObfuscate
    public class InternetPackListResult implements Serializable {
        public String rowID;
        public Integer serviceID;
        public String providerTitle;
        public String serviceName;
        public Integer servicePrice;
        public String profileName;
        public Integer profileTypeID;
        public String profileTitle;
    }
}
