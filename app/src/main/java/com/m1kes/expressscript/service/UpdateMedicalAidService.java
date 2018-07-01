package com.m1kes.expressscript.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.objects.MedicalAid;
import com.m1kes.expressscript.sqlite.adapters.MedicalAidDBAdapter;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;
import com.m1kes.expressscript.utils.parsers.MedicalAidJsonParser;

import java.util.List;

public class UpdateMedicalAidService extends IntentService {

    private static final String LOG_TAG = CheckMessagesService.class.getSimpleName();

    public UpdateMedicalAidService() {
        super(LOG_TAG);
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            String CHANNEL_ID = "my_channel_01";

            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            notificationManager.createNotificationChannel(mChannel);



            Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("")
                    .setAutoCancel(true);

            Notification notification = builder.build();
            startForeground(1, notification);


            Log.i(LOG_TAG, "Creating notification channel!!!");
        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent){
        Log.i(LOG_TAG, "Service is Running");

        WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
            @Override
            public void onSuccess(String response) {

                try {

                    List<MedicalAid> data = MedicalAidJsonParser.getMedicalAid(response);
                    if (data == null || data.isEmpty()) {
                        Log.i(LOG_TAG, "Unable to connect to server");
                        return;
                    }
                    //Setup persistant storage
                    MedicalAidDBAdapter.refill(data, getApplicationContext());

                    if (data.size() >= 1) {
                        Log.i(LOG_TAG, "Updated Medical Aid successfully");
                    }

                }catch (Exception ignore){}
            }

            @Override
            public void onFailed() {
                Log.i(LOG_TAG, "Unable to connect to server");
            }

            @Override
            public void onCompleteTask() {

            }
        });

        request.execute(EndPoints.API_URL + EndPoints.URL_MEDICAL_AID + ClientIDManager.getClientID(getApplicationContext()));

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        if (intent != null) {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
            System.out.println("Releasing Wakelock as Service is starting");
        }
    }


}
