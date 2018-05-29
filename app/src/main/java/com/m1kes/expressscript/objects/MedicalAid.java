package com.m1kes.expressscript.objects;

import org.json.simple.JSONObject;

import java.io.Serializable;

public class MedicalAid implements Serializable{

    private int id;
    private String name;
    private boolean assigned;

    public MedicalAid() {
    }

    public MedicalAid(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public static MedicalAid fromJsonObject(JSONObject map) {

        if(map == null)return null;

        int id = ((Long) map.get("Id")).intValue();
        String name = (String) map.get("Name");

        return new MedicalAid(id, name);

    }

    @Override
    public String toString() {
        return "MedicalAid{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", assigned=" + assigned +
                '}';
    }
}
