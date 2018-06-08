package com.m1kes.expressscript.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.m1kes.expressscript.service.UpdateMedicalAidService;

public class UpdateMedicalAidReciever extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12321;
    public static final String ACTION = "com.m1kes.expressscript.recievers.checkmessages";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, UpdateMedicalAidService.class);
        context.startService(i);
    }
}
