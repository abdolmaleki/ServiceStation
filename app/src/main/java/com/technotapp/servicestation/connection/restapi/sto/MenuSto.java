package com.technotapp.servicestation.connection.restapi.sto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class MenuSto extends BaseSto implements Parcelable {

    public List<DataModel> dataModel;
    public List<MessageModel> messageModel;

    protected MenuSto(Parcel in) {
        dataModel = in.createTypedArrayList(DataModel.CREATOR);
        messageModel = in.createTypedArrayList(MessageModel.CREATOR);
    }

    public static final Creator<MenuSto> CREATOR = new Creator<MenuSto>() {
        @Override
        public MenuSto createFromParcel(Parcel in) {
            return new MenuSto(in);
        }

        @Override
        public MenuSto[] newArray(int size) {
            return new MenuSto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(dataModel);
        dest.writeTypedList(messageModel);
    }

    public static class DataModel implements Serializable, Parcelable {
        public String tokenId;
        public List<Info> info;
        public List<Menu> menu;

        protected DataModel(Parcel in) {
            tokenId = in.readString();
            info = in.createTypedArrayList(Info.CREATOR);
            menu = in.createTypedArrayList(Menu.CREATOR);
        }

        public static Creator<DataModel> CREATOR = new Creator<DataModel>() {
            @Override
            public DataModel createFromParcel(Parcel in) {
                return new DataModel(in);
            }

            @Override
            public DataModel[] newArray(int size) {
                return new DataModel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(tokenId);
            dest.writeTypedList(info);
            dest.writeTypedList(menu);
        }
    }

    public static class MessageModel implements Serializable, Parcelable {

        public int errorCode;
        public String errorString;

        protected MessageModel(Parcel in) {
            errorCode = in.readInt();
            errorString = in.readString();
        }

        public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
            @Override
            public MessageModel createFromParcel(Parcel in) {
                return new MessageModel(in);
            }

            @Override
            public MessageModel[] newArray(int size) {
                return new MessageModel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(errorCode);
            dest.writeString(errorString);
        }
    }

    public static class Menu implements Serializable, Parcelable {
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

        protected Menu(Parcel in) {
            menuID = in.readInt();
            if (in.readByte() == 0) {
                parentMenuID = null;
            } else {
                parentMenuID = in.readInt();
            }
            title = in.readString();
            description = in.readString();
            icon = in.readString();
            url = in.readString();
            controller = in.readString();
            action = in.readString();
            status = in.readByte() != 0;
            if (in.readByte() == 0) {
                orderNo = null;
            } else {
                orderNo = in.readInt();
            }
            if (in.readByte() == 0) {
                orderNoSubMenu = null;
            } else {
                orderNoSubMenu = in.readInt();
            }
        }

        public static final Creator<Menu> CREATOR = new Creator<Menu>() {
            @Override
            public Menu createFromParcel(Parcel in) {
                return new Menu(in);
            }

            @Override
            public Menu[] newArray(int size) {
                return new Menu[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(menuID);
            if (parentMenuID == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(parentMenuID);
            }
            dest.writeString(title);
            dest.writeString(description);
            dest.writeString(icon);
            dest.writeString(url);
            dest.writeString(controller);
            dest.writeString(action);
            dest.writeByte((byte) (status ? 1 : 0));
            if (orderNo == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(orderNo);
            }
            if (orderNoSubMenu == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(orderNoSubMenu);
            }
        }
    }

    public static class Info implements Serializable, Parcelable {
        public String mobileNumber;
        public int isCustomer;
        public String firstName;
        public String lastName;
        public int gender;
        public String birthDate;
        public String email;
        public String address;
        public int score;

        protected Info(Parcel in) {
            mobileNumber = in.readString();
            isCustomer = in.readInt();
            firstName = in.readString();
            lastName = in.readString();
            gender = in.readInt();
            birthDate = in.readString();
            email = in.readString();
            address = in.readString();
            score = in.readInt();
        }

        public static final Creator<Info> CREATOR = new Creator<Info>() {
            @Override
            public Info createFromParcel(Parcel in) {
                return new Info(in);
            }

            @Override
            public Info[] newArray(int size) {
                return new Info[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mobileNumber);
            dest.writeInt(isCustomer);
            dest.writeString(firstName);
            dest.writeString(lastName);
            dest.writeInt(gender);
            dest.writeString(birthDate);
            dest.writeString(email);
            dest.writeString(address);
            dest.writeInt(score);
        }
    }

}
