package com.m1kes.expressscript.objects;

import java.util.List;

public class BranchDistance extends Branch {

    private Double distance;

    public BranchDistance(Branch branch, Double distance) {
        super(branch.getName(), branch.getAddress(), branch.getCity(), branch.getLatitude(), branch.getLongitude());
        this.distance = distance;
    }

    public BranchDistance(String name, String address, String city, double latitude, double longitude, List<String> contacts) {
        super(name, address, city, latitude, longitude);
    }

    public BranchDistance(String name, String address, String city, String telephone) {
        super(name, address, city, telephone);
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
