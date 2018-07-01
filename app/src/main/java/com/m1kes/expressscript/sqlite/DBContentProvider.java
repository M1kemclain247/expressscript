package com.m1kes.expressscript.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.m1kes.expressscript.sqlite.tables.MedicalAidTable;
import com.m1kes.expressscript.sqlite.tables.MessageTable;
import com.m1kes.expressscript.sqlite.tables.QuoteItemsTable;
import com.m1kes.expressscript.sqlite.tables.QuotesTable;
import com.m1kes.expressscript.sqlite.tables.ProductsTable;
import com.m1kes.expressscript.sqlite.tables.UserMedicalAid;


public class DBContentProvider extends ContentProvider {

    private DBHelper dbHelper ;

    public static final String AUTHORITY = "expscriptProviderAuthorities"; //specific for our our app, will be specified in manifest
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Create Content Uri's for the adapters to access
    public static final Uri MEDICAL_AID_URI = Uri.parse("content://" + AUTHORITY
            + "/" + MedicalAidTable.TABLE_NAME);

    public static final Uri USER_MEDICAL_AID_URI = Uri.parse("content://" + AUTHORITY
            + "/" + UserMedicalAid.TABLE_NAME);

    public static final Uri MESSAGES_URI = Uri.parse("content://" + AUTHORITY
            + "/" + MessageTable.TABLE_NAME);

    public static final Uri PRODUCTS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + ProductsTable.TABLE_NAME);

    public static final Uri QUOTES_URI = Uri.parse("content://" + AUTHORITY
            + "/" + QuotesTable.TABLE_NAME);

    public static final Uri QUOTE_ITEMS_URI = Uri.parse("content://" + AUTHORITY
            + "/" + QuoteItemsTable.TABLE_NAME);

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.query(table,  projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues initialValues) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long value = database.insert(table, null, initialValues);
        if(value>0){
            System.out.println("Successfully Inserted Using Content Provider Rows Affected: "+value);
        }else{
            System.out.println("Failed to insert record in content provider");
        }

        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] args) {
        String table = getTableName(uri);
        SQLiteDatabase dataBase=dbHelper.getWritableDatabase();
        return dataBase.delete(table, where, args);
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.update(table, values, whereClause, whereArgs);
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }


}