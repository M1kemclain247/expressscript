package com.m1kes.expressscript.objects;

/**
 * Created by shelton on 6/14/18.
 */

public class Branch {

    private double latitude;
    private double longitude;
    private String name;
    private String address;
    private String city;
private  String telephone;
    public Branch(String name, String address, String city, double latitude, double longitude) {
        this.address = address;
        this.name = name;
        this.latitude = latitude;
        this.city = city;
        this.longitude = longitude;
    }

    public Branch(String name, String address, String city, String telephone) {
        this.address = address;
        this.name = name;
        this.telephone = telephone;
        this.city = city;
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

    public String getCity() {
        return city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
