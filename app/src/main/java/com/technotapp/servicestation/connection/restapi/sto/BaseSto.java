package com.technotapp.servicestation.connection.restapi.sto;

import android.os.Parcel;
import android.os.Parcelable;

import com.technotapp.servicestation.Infrastructure.DontObfuscate;

import java.io.Serializable;
import java.util.List;

@DontObfuscate
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

    @DontObfuscate
    public static class MessageModel implements Serializable, Parcelable {

        public int errorCode;
        public String errorString;
        public int ver;


        protected MessageModel(Parcel in) {
            errorCode = in.readInt();
            errorString = in.readString();
            ver = in.readInt();
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
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(errorCode);
            parcel.writeString(errorString);
            parcel.writeInt(ver);
        }
    }

}
