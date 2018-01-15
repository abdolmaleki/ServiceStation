package com.technotapp.servicestation.connection.restapi.sto;

import android.os.Parcel;
import android.os.Parcelable;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.List;

@DontObfuscate
public class MenuSto extends BaseSto implements Parcelable {

    public List<DataModel> dataModel;

    protected MenuSto(Parcel in) {
        super();
        dataModel = in.createTypedArrayList(DataModel.CREATOR);
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
    }

    @DontObfuscate
    public static class DataModel implements Serializable, Parcelable {
        public String tokenId;
        public String terminalCode;
        public String url;
        public String idSeller;
        public String menuCategory;
        public List<Info> info;
        public List<Menu> menu;
        public List<Shop> shop;


        protected DataModel(Parcel in) {
            tokenId = in.readString();
            terminalCode = in.readString();
            url = in.readString();
            idSeller = in.readString();
            menuCategory = in.readString();
            info = in.createTypedArrayList(Info.CREATOR);
            menu = in.createTypedArrayList(Menu.CREATOR);
            shop = in.createTypedArrayList(Shop.CREATOR);
        }

        public static final Creator<DataModel> CREATOR = new Creator<DataModel>() {
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
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(tokenId);
            parcel.writeString(terminalCode);
            parcel.writeString(url);
            parcel.writeString(idSeller);
            parcel.writeString(menuCategory);
            parcel.writeTypedList(info);
            parcel.writeTypedList(menu);
            parcel.writeTypedList(shop);
        }

        @DontObfuscate
        public static class Info implements Serializable, Parcelable {
            public String hashId;
            public String firstName;
            public String lastName;
            public int gender;
            public String email;
            public int score;
            public String mobileNumber;


            protected Info(Parcel in) {
                mobileNumber = in.readString();
                firstName = in.readString();
                lastName = in.readString();
                gender = in.readInt();
                email = in.readString();
                hashId = in.readString();
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
                dest.writeString(firstName);
                dest.writeString(lastName);
                dest.writeString(hashId);
                dest.writeInt(gender);
                dest.writeString(email);
                dest.writeInt(score);
            }
        }

        @DontObfuscate
        public static class Shop implements Serializable, Parcelable {
            public String address;
            public String description;
            public String economicCode;
            public String email;
            public String fax;
            public String managerName;
            public String mobile;
            public String registerDate;
            public long registerNumber;
            public String shopCategory;
            public String tel;
            public String title;
            public int producVersion;


            protected Shop(Parcel in) {
                address = in.readString();
                description = in.readString();
                economicCode = in.readString();
                email = in.readString();
                fax = in.readString();
                managerName = in.readString();
                mobile = in.readString();
                registerDate = in.readString();
                registerNumber = in.readLong();
                shopCategory = in.readString();
                tel = in.readString();
                title = in.readString();
                producVersion = in.readInt();
            }

            public static final Creator<Shop> CREATOR = new Creator<Shop>() {
                @Override
                public Shop createFromParcel(Parcel in) {
                    return new Shop(in);
                }

                @Override
                public Shop[] newArray(int size) {
                    return new Shop[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeString(address);
                parcel.writeString(description);
                parcel.writeString(economicCode);
                parcel.writeString(email);
                parcel.writeString(fax);
                parcel.writeString(managerName);
                parcel.writeString(mobile);
                parcel.writeString(registerDate);
                parcel.writeLong(registerNumber);
                parcel.writeString(shopCategory);
                parcel.writeString(tel);
                parcel.writeString(title);
                parcel.writeInt(producVersion);
            }
        }

        @DontObfuscate
        public static class Menu implements Serializable, Parcelable {
            public int menuID;
            public Integer parentMenuID;
            public String title;
            public String description;
            public String icon;
            public String url;
            public String controller;
            public String action;
            public long serviceID;
            public int serviceTypeID;
            public String serviceTypeTitle;

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
                serviceID = in.readLong();
                serviceTypeID = in.readInt();
                serviceTypeTitle = in.readString();

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
                dest.writeLong(serviceID);
                dest.writeInt(serviceTypeID);
                dest.writeString(serviceTypeTitle);
            }
        }
    }


}
