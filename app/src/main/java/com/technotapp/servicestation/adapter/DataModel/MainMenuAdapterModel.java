package com.technotapp.servicestation.adapter.DataModel;

import com.technotapp.servicestation.database.model.MenuModel;

public class MainMenuAdapterModel {
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


    public MainMenuAdapterModel(MenuModel model) {
        this.title = model.title;
        this.icon = model.icon;
        this.action = model.action;
        this.controller = model.controller;
        this.description = model.description;
        this.orderNo = model.orderNo;
        this.parentMenuID = model.parentMenuID;
        this.url = model.url;
        this.status = model.status;
        this.id = model.id;
    }
}
