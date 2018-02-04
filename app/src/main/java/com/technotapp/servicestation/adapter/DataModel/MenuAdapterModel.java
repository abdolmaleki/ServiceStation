package com.technotapp.servicestation.adapter.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.technotapp.servicestation.database.model.MenuModel;

public class MenuAdapterModel implements Parcelable {
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

    protected MenuAdapterModel(Parcel in) {
        id = in.readInt();
        parentMenuID = in.readInt();
        title = in.readString();
        description = in.readString();
        icon = in.readString();
        url = in.readString();
        controller = in.readString();
        action = in.readString();
        orderNo = in.readInt();
    }

    public static final Creator<MenuAdapterModel> CREATOR = new Creator<MenuAdapterModel>() {
        @Override
        public MenuAdapterModel createFromParcel(Parcel in) {
            return new MenuAdapterModel(in);
        }

        @Override
        public MenuAdapterModel[] newArray(int size) {
            return new MenuAdapterModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(parentMenuID);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(icon);
        parcel.writeString(url);
        parcel.writeString(controller);
        parcel.writeString(action);
        parcel.writeInt(orderNo);
    }
}
