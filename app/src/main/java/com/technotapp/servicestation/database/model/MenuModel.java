package com.technotapp.servicestation.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MenuModel extends RealmObject {

    @PrimaryKey
    public int id;
    public int parentMenuID;
    public String title;
    public String description;
    public String icon;
    public String url;
    public String controller;
    public String action;
    public boolean status;
    public int orderNo;

}
