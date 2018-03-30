package com.taoze.basic.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Taoze on 2018/3/26.
 */
public class ChatInfo implements Parcelable, Serializable{

    public int picture;

    public String nickname;

    public String lastMsg;

    public String lastTime;

    public ChatInfo(){

    }

    protected ChatInfo(Parcel in) {
        picture = in.readInt();
        nickname = in.readString();
        lastMsg = in.readString();
        lastTime = in.readString();
    }

    public static final Creator<ChatInfo> CREATOR = new Creator<ChatInfo>() {
        @Override
        public ChatInfo createFromParcel(Parcel in) {
            return new ChatInfo(in);
        }

        @Override
        public ChatInfo[] newArray(int size) {
            return new ChatInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(picture);
        parcel.writeString(nickname);
        parcel.writeString(lastMsg);
        parcel.writeString(lastTime);
    }
}
