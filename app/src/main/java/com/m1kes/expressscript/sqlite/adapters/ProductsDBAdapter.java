package com.m1kes.expressscript.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.m1kes.expressscript.objects.Product;
import com.m1kes.expressscript.sqlite.DBContentProvider;
import com.m1kes.expressscript.sqlite.tables.MedicalAidTable;
import com.m1kes.expressscript.sqlite.tables.ProductsTable;
import com.m1kes.expressscript.sqlite.tables.UserMedicalAid;

import java.util.ArrayList;
import java.util.List;

public class ProductsDBAdapter {


    public static List<Product> getAll(Context context){

        List<Product> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.PRODUCTS_URI , null, null, null, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            Product item = new Product();

            int id = cursor.getInt(cursor.getColumnIndex(ProductsTable.PRODUCT_ID));

            item.setId(id);

            System.out.println("Loading from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static void add(Product item, Context context) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(ProductsTable.PRODUCT_ID, item.getId());
        System.out.println("Adding Bay : "+item.toString());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, ProductsTable.TABLE_NAME);
        Uri resultUri = context.getContentResolver().insert(contentUri, initialValues);

    }


    public static int update(Product item,Context context){

        ContentValues values = new ContentValues();
        values.put(ProductsTable.ID, item.getId());
        values.put(ProductsTable.PRODUCT_ID, item.getId());

        values.put(ProductsTable.DESCRIPTION, item.getDescription());
        values.put(ProductsTable.UNIT_PRICE, item.getUnit_price());
        values.put(ProductsTable.QUANTITY, item.getQuantity());
        values.put(ProductsTable.TOTAL, item.getTotal_price());

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI,ProductsTable.TABLE_NAME);

        System.out.println("Updating record using id :  " + item.getId());
        int rowsAffected =  context.getContentResolver().update(contentUri,values,
                MedicalAidTable.ID+"=?",
                new String[]{item.getId() + ""});

        System.out.println("Updated Rows: "+ rowsAffected);

        return rowsAffected;
    }


    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, ProductsTable.TABLE_NAME);
        int result = context.getContentResolver().delete(contentUri,null,null);

        System.out.println("Deleted status: "+result);
    }

    public static void refill(List<Product> data, Context context) {
        deleteAll(context);
        for(Product item : data){
            add(item,context);
        }
    }


}
