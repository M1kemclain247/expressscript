package com.m1kes.expressscript.sqlite.adapters;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


import com.m1kes.expressscript.objects.Quote;
import com.m1kes.expressscript.objects.QuoteItem;
import com.m1kes.expressscript.sqlite.DBContentProvider;
import com.m1kes.expressscript.sqlite.tables.QuotesTable;
import com.m1kes.expressscript.utils.CoreUtils;

import java.util.ArrayList;
import java.util.List;

public class QuotesDBAdapter {

    public static List<Quote> getAll(Context context){

        List<Quote> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.QUOTES_URI, null, null, null, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            Quote item = new Quote();

            int id = cursor.getInt(cursor.getColumnIndex(QuotesTable.ID));
            String content = cursor.getString(cursor.getColumnIndex(QuotesTable.CONTENT));
            String quotation_details = cursor.getString(cursor.getColumnIndex(QuotesTable.QUOTATION_DETAILS));
            boolean isSynced = CoreUtils.toBoolean(cursor.getInt(cursor.getColumnIndex(QuotesTable.IS_SYNCED)));

            item.setId(id);
            item.setContent(content);
            item.setQuotationDetails(quotation_details);
            item.setSynced(isSynced);


            List<QuoteItem> items = QuoteItemsDBAdapter.getForQuote(item,context);
            item.setItems(items);
            System.out.println("Loading from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static List<Quote> getUnsynced(Context context){

        List<Quote> data = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query (DBContentProvider.QUOTES_URI, null, QuotesTable.IS_SYNCED+"=?", new String[]{""+0}, null);

        if(cursor == null || cursor.getCount() < 1) return data;

        while (cursor.moveToNext()) {
            // Gets the value from the column.
            Quote item = new Quote();

            int id = cursor.getInt(cursor.getColumnIndex(QuotesTable.ID));
            String content = cursor.getString(cursor.getColumnIndex(QuotesTable.CONTENT));
            String quotation_details = cursor.getString(cursor.getColumnIndex(QuotesTable.QUOTATION_DETAILS));
            boolean isSynced = CoreUtils.toBoolean(cursor.getInt(cursor.getColumnIndex(QuotesTable.IS_SYNCED)));


            item.setId(id);
            item.setContent(content);
            item.setQuotationDetails(quotation_details);
            item.setSynced(isSynced);

            List<QuoteItem> items = QuoteItemsDBAdapter.getForQuote(item,context);
            item.setItems(items);

            System.out.println("Loading from DB: "+item.toString());

            data.add(item);
        }

        cursor.close();

        return data;
    }


    public static void add(Quote item , Context context) {

        ContentValues values = new ContentValues();
        values.put(QuotesTable.ID, item.getId());
        values.put(QuotesTable.CONTENT,item.getContent());
        values.put(QuotesTable.QUOTATION_DETAILS,item.getQuotationDetails());
        values.put(QuotesTable.IS_SYNCED, CoreUtils.toInt(item.isSynced()));
        System.out.println("Adding Quote : "+item.toString());
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, QuotesTable.TABLE_NAME);
        Uri resultUri = context.getContentResolver().insert(contentUri, values);

    }


    public static int update(Quote item , Context context){

        ContentValues values = new ContentValues();
        values.put(QuotesTable.ID, item.getId());
        values.put(QuotesTable.CONTENT, item.getContent());
        values.put(QuotesTable.QUOTATION_DETAILS,item.getQuotationDetails());
        values.put(QuotesTable.IS_SYNCED, CoreUtils.toInt(item.isSynced()));
        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, QuotesTable.TABLE_NAME);

        System.out.println("Updating record using id :  " + item.getId());
        int rowsAffected =  context.getContentResolver().update(contentUri,values,
                QuotesTable.ID+"=?",
                new String[]{item.getId() + ""});

        System.out.println("Updated Rows: "+ rowsAffected);

        for(QuoteItem quoteItem : item.getItems()){
            QuoteItemsDBAdapter.update(quoteItem,context);
        }

        return rowsAffected;
    }


    private static void deleteAll(Context context){

        Uri contentUri = Uri.withAppendedPath(DBContentProvider.CONTENT_URI, QuotesTable.TABLE_NAME);
        int result = context.getContentResolver().delete(contentUri,null,null);

        System.out.println("Deleted status: "+result);
    }

    public static void refill(List<Quote> data, Context context) {
        deleteAll(context);
        for(Quote item : data){
            add(item,context);
        }
    }


}
