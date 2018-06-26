package com.m1kes.expressscript.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

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
