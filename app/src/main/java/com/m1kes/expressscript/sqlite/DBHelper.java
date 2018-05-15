package com.m1kes.expressscript.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.m1kes.expressscript.sqlite.tables.MedicalAidTable;
import com.m1kes.expressscript.sqlite.tables.MessageTable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "express_script.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        MedicalAidTable.onCreate(sqLiteDatabase);
        MessageTable.onCreate(sqLiteDatabase);
        System.out.println("Database's Created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        MedicalAidTable.onUpgrade(sqLiteDatabase, i, i1);
        MessageTable.onUpgrade(sqLiteDatabase,i,i1);
        System.out.println("Database's Upgraded");
    }






}
