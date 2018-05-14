package com.m1kes.expressscript.objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Parcelable{

    private int id;
    private int client_id;
    private String content;
    private Bitmap bitmap;
    private String file_path;

    public Message(int id, int client_id, String content, Bitmap bitmap) {
        this.id = id;
        this.client_id = client_id;
        this.content = content;
        this.bitmap = bitmap;
    }

    public Message(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Message(String file_path) {
        this.file_path = file_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
