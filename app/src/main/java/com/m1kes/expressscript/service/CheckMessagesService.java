package com.m1kes.expressscript.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.i(LOG_TAG, "Service is Running");

        if(ClientIDManager.getClientID(getApplicationContext()) == 0)return;


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

        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}
