package com.m1kes.expressscript.storage;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class ClientIDManager {


    private static final String CLIENT_ID = "CLIENT_ID";
    private static final String APP_STORAGE = "ClientSettings";


    public static void setClientId(Context context,int client_id){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_STORAGE, MODE_PRIVATE).edit();
        editor.putInt(CLIENT_ID,client_id);
        editor.apply();
    }

    public static int getClientID(Context context){
        SharedPreferences prefs = context.getSharedPreferences(APP_STORAGE, MODE_PRIVATE);
        return prefs.getInt(CLIENT_ID, 0);
    }


}
