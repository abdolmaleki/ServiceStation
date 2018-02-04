package com.technotapp.servicestation.adapter.DataModel;

public class SettingAdapterModel {

    public SettingAdapterModel(String title, int icon, long actionType) {
        this.title = title;
        this.icon = icon;
        this.actionType = actionType;
    }

    public String title;
    public int icon;
    public long actionType;
}
