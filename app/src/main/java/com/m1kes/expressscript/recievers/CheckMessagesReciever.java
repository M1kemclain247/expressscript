package com.m1kes.expressscript.recievers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.m1kes.expressscript.service.CheckMessagesService;

public class CheckMessagesReciever extends WakefulBroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.m1kes.expressscript.recievers.checkmessages";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, CheckMessagesService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
