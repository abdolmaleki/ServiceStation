package com.technotapp.servicestation.connection.restapi.sto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuSto extends BaseSto {

    List<DataModel> dataModel = new ArrayList<>();
    List<MessageModel> messageModel = new ArrayList<>();

    public class DataModel implements Serializable {
        public String tokenId;
        public List<Info> info;
        public List<Menu> menu;
    }

    public class MessageModel implements Serializable {

        public int errorCode;
        public String errorString;
    }

    public class Menu implements Serializable {
        public int menuID;
        public Integer parentMenuID;
        public String title;
        public String description;
        public String icon;
        public String url;
        public String controller;
        public String action;
        public boolean status;
        public Integer orderNo;
        public Integer orderNoSubMenu;
    }

    public class Info implements Serializable {
        public String mobileNumber;
        public int isCustomer;
        public String firstName;
        public String lastName;
        public int gender;
        public String birthDate;
        public String email;
        public String address;
        public int score;
    }

}
