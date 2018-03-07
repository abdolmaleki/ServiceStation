package com.technotapp.servicestation.connection.restapi.sto;

import java.io.Serializable;
import java.util.List;

public class SettingLoginSto extends BaseSto {
    public List<DataModel> dataModel = null;

    public class DataModel implements Serializable {
        public List<LoginData> data = null;

        public class LoginData implements Serializable {

            public String name;
            public String lastName;
            public String mobileNumber;
            public int gender;
            public String tokenId;
            public String tokenExpireDate;
            public String url;
            public String imageName;
        }
    }
}
