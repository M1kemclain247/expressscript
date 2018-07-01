package com.m1kes.expressscript.sqlite.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class QuoteItemsTable {

    public static final String TABLE_NAME = "quote_items";

    public static final String ID = "id";
    public static final String QUOTE_ID = "quote_id";
    public static final String DESCRIPTION = "description";
    public static final String UNIT_PRICE = "unit_price";
    public static final String QUANTITY = "quantity";
    public static final String TOTAL = "total";

    private static final String TABLE_CREATE = "create table "
            + TABLE_NAME
            + "("
            + ID + " INTEGER NOT NULL, "
            + QUOTE_ID + " INTEGER , "
            + DESCRIPTION + " TEXT , "
            + UNIT_PRICE + " REAL , "
            + QUANTITY + " REAL , "
            + TOTAL + " REAL "
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
