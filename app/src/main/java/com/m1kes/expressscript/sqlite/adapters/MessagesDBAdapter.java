package com.m1kes.expressscript.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.sqlite.DBContentProvider;
import com.m1kes.expressscript.sqlite.tables.MessageTable;

import java.util.ArrayList;
import java.util.List;

public class MessagesDBAdapter {

    public static List<Message> getAll(Context context){

        List<Message> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.MESSAGES_URI , null, null, null, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex(MessageTable.ID));
            String content = cursor.getString(cursor.getColumnIndex(MessageTable.CONTENT));

            data.add(new Message(id,content));
        }

        cursor.close();

        return data;
    }


    public static void add(Message msg, Context context) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(MessageTable.ID, msg.getId());
        initialValues.put(MessageTable.CONTENT, msg.getContent());

        System.out.println("Adding Message : "+msg.toString());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, MessageTable.TABLE_NAME);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);

        System.out.println("Added A MSG Successfully");
    }

    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, MessageTable.TABLE_NAME);
        int result = context.getContentResolver().delete(contentUri,null,null);

        System.out.println("Deleted status: "+result);
    }

    public static void refill(List<Message> data, Context context) {
        deleteAll(context);
        for(Message item : data){
            add(item,context);
        }
    }



}
