package com.m1kes.expressscript.utils.extra;


import android.os.Parcel;
import android.os.Parcelable;

public class MapLocation implements Parcelable {


    public static final Creator<MapLocation> CREATOR = new Creator() {
        public MapLocation createFromParcel(Parcel in) {
            return new MapLocation(in);
        }

        public MapLocation[] newArray(int size) {
            return new MapLocation[size];
        }
    };


    private double latitude;
    private double longitude;
    private String name;
    private String address;

    public MapLocation(double latitude, double longitude, String name, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
    }

    public MapLocation(Parcel parcel) {
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
        name = parcel.readString();
        address = parcel.readString();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(name);
        dest.writeString(address);
    }
}
