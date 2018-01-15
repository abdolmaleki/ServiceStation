package com.technotapp.servicestation.adapter.DataModel;

import com.technotapp.servicestation.database.model.MenuModel;

public class MenuAdapterModel {
    public int id;
    public int parentMenuID;
    public String title;
    public String description;
    public String icon;
    public String url;
    public String controller;
    public String action;
    public int orderNo;


    public MenuAdapterModel(MenuModel model) {
        this.title = model.title;
        this.icon = model.icon;
        this.action = model.action;
        this.controller = model.controller;
        this.description = model.description;
        this.orderNo = model.orderNo;
        this.parentMenuID = model.parentMenuID;
        this.url = model.url;
        this.id = model.id;
    }
}
