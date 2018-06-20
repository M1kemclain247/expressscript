package com.m1kes.expressscript.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class OrdersTable {


    public static final String TABLE_NAME = "user_orders";

    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String QUOTATION_DETAILS = "quotation_details";
    public static final String IS_SYNCED = "synced";

    private static final String TABLE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " INTEGER NOT NULL, "
            + CONTENT + " TEXT , "
            + QUOTATION_DETAILS + " TEXT , "
            + IS_SYNCED + " INTEGER"
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
