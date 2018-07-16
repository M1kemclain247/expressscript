package com.m1kes.expressscript.storage;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class OrderRefGenerator {

    private static final String CLIENT_ID = "ORDER_REF";
    private static final String APP_STORAGE = "ClientSettings";


    private static void set(Context context, int client_id){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_STORAGE, MODE_PRIVATE).edit();
        editor.putInt(CLIENT_ID,client_id);
        editor.apply();
    }

    private static int get(Context context){
        SharedPreferences prefs = context.getSharedPreferences(APP_STORAGE, MODE_PRIVATE);
        return prefs.getInt(CLIENT_ID, 0);
    }

    public static int getNextOrderNo(Context context){
        int val = get(context) + 1;
        set(context,val);
        return val;
    }
}
