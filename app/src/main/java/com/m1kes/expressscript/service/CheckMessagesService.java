package com.m1kes.expressscript.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.m1kes.expressscript.R;
import com.m1kes.expressscript.SplashScreen;
import com.m1kes.expressscript.objects.Message;
import com.m1kes.expressscript.storage.ClientIDManager;
import com.m1kes.expressscript.utils.EndPoints;
import com.m1kes.expressscript.utils.WebUtils;
import com.m1kes.expressscript.utils.parsers.MessageJsonParser;

import java.util.List;

public class CheckMessagesService extends IntentService {

    private String LOG_TAG = CheckMessagesService.class.getSimpleName();


    public CheckMessagesService() {
        super("CheckMessagesService");
    }


    @Override
    public void onCreate() {
        super.onCreate();


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
    protected void onHandleIntent(@Nullable final Intent intent) {

        Log.i(LOG_TAG, "Service is Running");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(intent);
        }

        if(ClientIDManager.getClientID(getApplicationContext()) == 0)return;


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                WebUtils.SimpleHttpURLWebRequest request = WebUtils.getSimpleHttpRequest(new WebUtils.OnResponseCallback() {
                    @Override
                    public void onSuccess(String response) {

                        try {

                            List<Message> new_content = MessageJsonParser.getMessages(response);
                            if (new_content == null) {
                                Log.i(LOG_TAG, "Content downloaded is null!");
                                return;
                            }

                            Log.i(LOG_TAG, "Downloaded :" + new_content.size() + " New messages!");


                            if (new_content.size() >= 1) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    startForegroundService(intent);
                                }

                                showForegroundNotification(new_content);
                            }
                        }catch (Exception ignore){ }

                    }

                    @Override
                    public void onFailed() {
                        Log.i(LOG_TAG, "Failed to connect to server!");
                    }

                    @Override
                    public void onCompleteTask() {

                    }
                });

                request.execute(EndPoints.API_URL + EndPoints.API_GET_ALL_MESSAGES + ClientIDManager.getClientID(getApplicationContext()));

                // Release the wake lock provided by the WakefulBroadcastReceiver.
                if (intent != null) {
                    WakefulBroadcastReceiver.completeWakefulIntent(intent);
                    System.out.println("Releasing Wakelock as Service is starting");
                }
            }
        });

    }

    private void showForegroundNotification(List<Message> messages) {
        if(messages.size() == 1){
            pushNotification("New Message",messages.get(0).getContent());
        }else{
            pushNotification(messages.size() + " New Messages","Click here to view them");
        }
    }

    private void pushNotification(String title,String content) {

        int NOTIFICATION_ID = 234;

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "my_channel_01";

        Log.i(LOG_TAG, "going to be sending a notification now!");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);

            Log.i(LOG_TAG, "Creating notification channel!!!");
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content);

        Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(SplashScreen.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        startForeground(NOTIFICATION_ID,builder.build());

        Log.i(LOG_TAG, "Starting foreground notification!");

  //      this.startForeground(NOTIFICATION_ID,builder.build());
//
        //notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
