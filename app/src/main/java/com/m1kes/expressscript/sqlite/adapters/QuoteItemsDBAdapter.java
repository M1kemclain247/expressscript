package com.m1kes.expressscript.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.m1kes.expressscript.objects.Quote;
import com.m1kes.expressscript.objects.QuoteItem;
import com.m1kes.expressscript.sqlite.DBContentProvider;
import com.m1kes.expressscript.sqlite.tables.QuoteItemsTable;
import com.m1kes.expressscript.sqlite.tables.QuotesTable;

import java.util.ArrayList;
import java.util.List;

public class QuoteItemsDBAdapter {

    public static List<QuoteItem> getAll(Context context){

        List<QuoteItem> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.QUOTE_ITEMS_URI, null, null, null, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            QuoteItem item = new QuoteItem();

            int id = cursor.getInt(cursor.getColumnIndex(QuoteItemsTable.ID));
            int quote_id = cursor.getInt(cursor.getColumnIndex(QuoteItemsTable.QUOTE_ID));
            String description = cursor.getString(cursor.getColumnIndex(QuoteItemsTable.DESCRIPTION));
            double unit_price = cursor.getDouble(cursor.getColumnIndex(QuoteItemsTable.UNIT_PRICE));
            double quantity = cursor.getDouble(cursor.getColumnIndex(QuoteItemsTable.QUANTITY));
            double total = cursor.getDouble(cursor.getColumnIndex(QuoteItemsTable.TOTAL));

            item.setQuoteId(quote_id);
            item.setProductID(id);
            item.setDescription(description);
            item.setUnitPrice(unit_price);
            item.setQuantity(quantity);
            item.setTotal(total);

            System.out.println("Loading from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }

    public static List<QuoteItem> getForQuote(Quote quote , Context context){

        List<QuoteItem> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        System.out.println("Searching for Quote items using Quote ID : " + quote.getId());
        Cursor cursor = cr.query (DBContentProvider.QUOTE_ITEMS_URI, null, QuoteItemsTable.QUOTE_ID+"=?", new String[]{""+quote.getId()}, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            QuoteItem item = new QuoteItem();

            int id = cursor.getInt(cursor.getColumnIndex(QuoteItemsTable.ID));
            int quote_id = cursor.getInt(cursor.getColumnIndex(QuoteItemsTable.QUOTE_ID));
            String description = cursor.getString(cursor.getColumnIndex(QuoteItemsTable.DESCRIPTION));
            double unit_price = cursor.getDouble(cursor.getColumnIndex(QuoteItemsTable.UNIT_PRICE));
            double quantity = cursor.getDouble(cursor.getColumnIndex(QuoteItemsTable.QUANTITY));
            double total = cursor.getDouble(cursor.getColumnIndex(QuoteItemsTable.TOTAL));

            item.setQuoteId(quote_id);
            item.setProductID(id);
            item.setDescription(description);
            item.setUnitPrice(unit_price);
            item.setQuantity(quantity);
            item.setTotal(total);

            System.out.println("Loading Quote Item from DB: " + item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static void add(QuoteItem item , Context context) {

        ContentValues values = new ContentValues();
        values.put(QuoteItemsTable.ID, item.getProductID());
        values.put(QuoteItemsTable.QUOTE_ID,item.getQuoteId());
        values.put(QuoteItemsTable.DESCRIPTION,item.getDescription());
        values.put(QuoteItemsTable.UNIT_PRICE, item.getUnitPrice());
        values.put(QuoteItemsTable.QUANTITY,item.getQuantity());
        values.put(QuoteItemsTable.TOTAL, item.getTotal());
        System.out.println("Adding QuoteItem : "+item.toString());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, QuoteItemsTable.TABLE_NAME);
        Uri resultUri = context.getContentResolver().insert(contentUri, values);

    }


    public static int update(QuoteItem item , Context context){

        ContentValues values = new ContentValues();
        values.put(QuoteItemsTable.ID, item.getProductID());
        values.put(QuoteItemsTable.QUOTE_ID,item.getQuoteId());
        values.put(QuoteItemsTable.DESCRIPTION,item.getDescription());
        values.put(QuoteItemsTable.UNIT_PRICE, item.getUnitPrice());
        values.put(QuoteItemsTable.QUANTITY,item.getQuantity());
        values.put(QuoteItemsTable.TOTAL, item.getTotal());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, QuoteItemsTable.TABLE_NAME);

        int rowsAffected =  context.getContentResolver().update(contentUri,values,
                QuoteItemsTable.ID+"=?",
                new String[]{item.getProductID() + ""});

        System.out.println("Updating Quote item with ID : " + item.getProductID());


        if(rowsAffected <= 0){
            add(item,context);
        }

        return rowsAffected;
    }


    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, QuotesTable.TABLE_NAME);
        int result = context.getContentResolver().delete(contentUri,null,null);

        System.out.println("Deleted status: "+result);
    }

    public static void refill(List<QuoteItem> data, Context context) {
        deleteAll(context);
        for(QuoteItem item : data){
            add(item,context);
        }
    }

}
