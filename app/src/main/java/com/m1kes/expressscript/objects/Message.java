package com.m1kes.expressscript.objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.m1kes.expressscript.objects.custom.CustomDate;

import org.json.simple.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Parcelable{

    private int id;
    private int client_id;
    private String content;
    private Bitmap bitmap;
    private String sender;
    private CustomDate date;

    public Message(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public Message(int id, String content, String sender,CustomDate date) {
        this.id = id;
        this.content = content;
        this.sender = sender;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getCreationTime() {
        return date.getShortTime();
    }

    public CustomDate getDate() {
        return date;
    }

    public static Message fromJsonObject(JSONObject map) {

        if(map == null)return null;

        int id = ((Long) map.get("Id")).intValue();
        String message = (String) map.get("Message");
        String sender = "Server";

        return new Message(id, message,sender,new CustomDate());

    }

    public Message(Parcel in) {
        id = in.readInt();
        client_id = in.readInt();
        content = in.readString();
        bitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(client_id);
        dest.writeString(content);
        dest.writeValue(bitmap);
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

}
