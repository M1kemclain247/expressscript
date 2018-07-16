package com.m1kes.expressscript.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.m1kes.expressscript.objects.OrderWrapper;
import com.m1kes.expressscript.sqlite.DBContentProvider;
import com.m1kes.expressscript.sqlite.tables.OrdersTable;

import java.util.ArrayList;
import java.util.List;

public class OrdersDBAdapter {

    public static List<OrderWrapper> getAll(Context context){

        List<OrderWrapper> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.ORDERS_URI , null, null, null, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {

            String jsonContent = cursor.getString(cursor.getColumnIndex(OrdersTable.CONTENT));
            String status = cursor.getString(cursor.getColumnIndex(OrdersTable.STATUS));
            OrderWrapper item = OrderWrapper.fromJson(jsonContent);

            System.out.println("Loading from DB: "+item.toString());
            item.setStatus(status);

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static void add(OrderWrapper item, Context context) {

        ContentValues values = new ContentValues();
        values.put(OrdersTable.ID, item.getId());
        values.put(OrdersTable.CONTENT, item.toJson(item));
        values.put(OrdersTable.STATUS, item.getStatus());

        System.out.println("Adding Order : "+item.toString());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, OrdersTable.TABLE_NAME);

        Uri resultUri = context.getContentResolver().insert(contentUri, values);
    }


    public static int update(OrderWrapper item,Context context){

        ContentValues values = new ContentValues();
        values.put(OrdersTable.ID, item.getId());
        values.put(OrdersTable.CONTENT, item.toJson(item));
        values.put(OrdersTable.STATUS, item.getStatus());

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI,OrdersTable.TABLE_NAME);

        System.out.println("Updating record using id :  " + item.getId());
        int rowsAffected =  context.getContentResolver().update(contentUri,values,
                OrdersTable.ID+"=?",
                new String[]{item.getId() + ""});

        System.out.println("Updated Rows: "+ rowsAffected);

        return rowsAffected;
    }


    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, OrdersTable.TABLE_NAME);
        int result = context.getContentResolver().delete(contentUri,null,null);

        System.out.println("Deleted status: "+result);
    }

    public static void refill(List<OrderWrapper> data, Context context) {
        deleteAll(context);
        for(OrderWrapper item : data){
            add(item,context);
        }
    }


}
