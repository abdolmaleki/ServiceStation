package com.technotapp.servicestation.connection.restapi.sto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;


public class BaseSto implements Serializable, Parcelable {
    public List<MenuSto.MessageModel> messageModel;


    protected BaseSto(Parcel in) {
        messageModel = in.createTypedArrayList(MessageModel.CREATOR);
    }

    public static final Creator<BaseSto> CREATOR = new Creator<BaseSto>() {
        @Override
        public BaseSto createFromParcel(Parcel in) {
            return new BaseSto(in);
        }

        @Override
        public BaseSto[] newArray(int size) {
            return new BaseSto[size];
        }
    };

    public BaseSto() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(messageModel);
    }

    public static class MessageModel implements Serializable, Parcelable {

        public int errorCode;
        public String errorString;
        public int ver;


        protected MessageModel(Parcel in) {
            ver = in.readInt();
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
            dest.writeInt(ver);
            dest.writeString(errorString);
        }
    }

}
