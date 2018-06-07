package com.m1kes.expressscript.sqlite.tables;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MedicalAidTable {

    // Database table
    public static final String TABLE_NAME = "user_medical_aids";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ASSIGNED = "assigned";

    // Database creation SQL statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " INTEGER PRIMARY KEY, "
            + NAME + " TEXT NOT NULL ,"
            + ASSIGNED + " INTEGER NOT NULL"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(MedicalAidTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }



}