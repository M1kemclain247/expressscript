package com.m1kes.expressscript.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.renderscript.ScriptIntrinsicYuvToRGB;


import com.m1kes.expressscript.objects.Order;
import com.m1kes.expressscript.objects.Product;
import com.m1kes.expressscript.sqlite.DBContentProvider;
import com.m1kes.expressscript.sqlite.tables.MedicalAidTable;
import com.m1kes.expressscript.sqlite.tables.OrdersTable;
import com.m1kes.expressscript.sqlite.tables.ProductsTable;
import com.m1kes.expressscript.utils.CoreUtils;

import java.util.ArrayList;
import java.util.List;

public class OrdersDBAdapter {

    public static List<Order> getAll(Context context){

        List<Order> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.ORDERS_URI , null, null, null, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            Order item = new Order();

            int id = cursor.getInt(cursor.getColumnIndex(OrdersTable.ID));
            String content = cursor.getString(cursor.getColumnIndex(OrdersTable.CONTENT));
            String quotation_details = cursor.getString(cursor.getColumnIndex(OrdersTable.QUOTATION_DETAILS));
            boolean isSynced = CoreUtils.toBoolean(cursor.getInt(cursor.getColumnIndex(OrdersTable.IS_SYNCED)));

            item.setId(id);
            item.setContent(content);
            item.setQuotationDetails(quotation_details);
            item.setSynced(isSynced);

            System.out.println("Loading from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static List<Order> getUnsynced(Context context){

        List<Order> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.ORDERS_URI , null, OrdersTable.IS_SYNCED+"=?", new String[]{""+0}, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            Order item = new Order();

            int id = cursor.getInt(cursor.getColumnIndex(OrdersTable.ID));
            String content = cursor.getString(cursor.getColumnIndex(OrdersTable.CONTENT));
            String quotation_details = cursor.getString(cursor.getColumnIndex(OrdersTable.QUOTATION_DETAILS));
            boolean isSynced = CoreUtils.toBoolean(cursor.getInt(cursor.getColumnIndex(OrdersTable.IS_SYNCED)));


            item.setId(id);
            item.setContent(content);
            item.setQuotationDetails(quotation_details);
            item.setSynced(isSynced);

            System.out.println("Loading from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static void add(Order item , Context context) {

        ContentValues values = new ContentValues();
        values.put(OrdersTable.ID, item.getId());
        values.put(OrdersTable.CONTENT,item.getContent());
        values.put(OrdersTable.QUOTATION_DETAILS,item.getQuotationDetails());
        values.put(OrdersTable.IS_SYNCED, CoreUtils.toInt(item.isSynced()));
        System.out.println("Adding Order : "+item.toString());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, OrdersTable.TABLE_NAME);
        Uri resultUri = context.getContentResolver().insert(contentUri, values);

    }


    public static int update(Order item ,Context context){

        ContentValues values = new ContentValues();
        values.put(OrdersTable.ID, item.getId());
        values.put(OrdersTable.CONTENT, item.getContent());
        values.put(OrdersTable.QUOTATION_DETAILS,item.getQuotationDetails());
        values.put(OrdersTable.IS_SYNCED, CoreUtils.toInt(item.isSynced()));
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

    public static void refill(List<Order> data, Context context) {
        deleteAll(context);
        for(Order item : data){
            add(item,context);
        }
    }


}
