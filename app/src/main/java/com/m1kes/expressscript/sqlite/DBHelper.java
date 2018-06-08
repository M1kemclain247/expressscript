package com.m1kes.expressscript.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.m1kes.expressscript.sqlite.tables.MedicalAidTable;
import com.m1kes.expressscript.sqlite.tables.MessageTable;
import com.m1kes.expressscript.sqlite.tables.UserMedicalAid;

public class DBHelper extends SQLiteOpenHelper {

    private final static String LOG_TAG = "Database";

    private static final String DATABASE_NAME = "express_script.db";
    private static String DATABASE_PATH = "";
    private static final int DATABASE_VERSION = 11;
    private static Context context;


    public DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);

        context = ctx;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();
        Log.e(LOG_TAG,"DB Path : " + DATABASE_PATH);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        MedicalAidTable.onCreate(sqLiteDatabase);
        MessageTable.onCreate(sqLiteDatabase);
        UserMedicalAid.onCreate(sqLiteDatabase);
        Log.e(LOG_TAG,"OnCreate called for database and tables!");
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        MedicalAidTable.onUpgrade(sqLiteDatabase, old_version, new_version);
        MessageTable.onUpgrade(sqLiteDatabase, old_version, new_version);
        UserMedicalAid.onUpgrade(sqLiteDatabase,old_version,new_version);
        Log.e(LOG_TAG,"OnUpgrade called for database and tables!");
    }






}
