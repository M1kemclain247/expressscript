package com.m1kes.expressscript.sqlite.adapters;

public class BaseAdapter {

    private static MedicalAidDBAdapter medicalAidDBAdapter;

    static{
        medicalAidDBAdapter = new MedicalAidDBAdapter();


    }


    public static MedicalAidDBAdapter getMedicalAidDBAdapter() {
        return medicalAidDBAdapter;
    }
}
