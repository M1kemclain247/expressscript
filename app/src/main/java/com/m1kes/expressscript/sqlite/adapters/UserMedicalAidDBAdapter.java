package com.m1kes.expressscript.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.sqlite.DBContentProvider;
import com.m1kes.expressscript.sqlite.tables.MedicalAidTable;
import com.m1kes.expressscript.sqlite.tables.UserMedicalAid;
import com.m1kes.expressscript.utils.CoreUtils;

import java.util.ArrayList;
import java.util.List;

public class UserMedicalAidDBAdapter {

    public static List<MedicalAid> getAll(Context context){

        List<MedicalAid> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.USER_MEDICAL_AID_URI , null, null, null, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            MedicalAid item = new MedicalAid();

            int id = cursor.getInt(cursor.getColumnIndex(MedicalAidTable.ID));
            String name = cursor.getString(cursor.getColumnIndex (MedicalAidTable.NAME));
            boolean assigned = CoreUtils.toBoolean(cursor.getInt(cursor.getColumnIndex(MedicalAidTable.ASSIGNED)));

            item.setId(id);
            item.setName(name);
            item.setAssigned(assigned);

            System.out.println("Loading from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }

    public static List<MedicalAid> getAssigned(boolean isAssigned, Context context){

        List<MedicalAid> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.USER_MEDICAL_AID_URI , null, UserMedicalAid.ASSIGNED + "=?",new String[]{CoreUtils.toInt(isAssigned) + ""} , null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            MedicalAid item = new MedicalAid();

            int id = cursor.getInt(cursor.getColumnIndex(MedicalAidTable.ID));
            String name = cursor.getString(cursor.getColumnIndex (MedicalAidTable.NAME));
            boolean assigned = CoreUtils.toBoolean(cursor.getInt(cursor.getColumnIndex(MedicalAidTable.ASSIGNED)));

            item.setId(id);
            item.setName(name);
            item.setAssigned(assigned);

            System.out.println("Fetching from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static void add(MedicalAid item, Context context) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(MedicalAidTable.ID, item.getId());
        initialValues.put(MedicalAidTable.NAME, item.getName());
        initialValues.put(MedicalAidTable.ASSIGNED,CoreUtils.toInt(item.isAssigned()));

        System.out.println("Adding Bay : "+item.toString());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, UserMedicalAid.TABLE_NAME);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);

    }


    public static int update(MedicalAid aid,Context context){

        ContentValues values = new ContentValues();
        values.put(MedicalAidTable.ID, aid.getId());
        values.put(MedicalAidTable.NAME, aid.getName());
        values.put(MedicalAidTable.ASSIGNED,CoreUtils.toInt(aid.isAssigned()));

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI,UserMedicalAid.TABLE_NAME);

        System.out.println("Updating record using id :  " + aid.getId());
        int rowsAffected =  context.getContentResolver().update(contentUri,values,
                MedicalAidTable.ID+"=?",
                new String[]{aid.getId() + ""});

        System.out.println("Updated Rows: "+ rowsAffected);

        return rowsAffected;
    }


    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, UserMedicalAid.TABLE_NAME);
        int result = context.getContentResolver().delete(contentUri,null,null);

        System.out.println("Deleted status: "+result);
    }

    public static void refill(List<MedicalAid> data, Context context) {
        deleteAll(context);
        for(MedicalAid item : data){
            add(item,context);
        }
    }


}
