package com.byteshaft.kidmonitor.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.services.RegistrationIntentService;
import com.byteshaft.kidmonitor.services.UploadService;
import com.byteshaft.kidmonitor.utils.Helpers;

public class ConnectionChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(AppGlobals.getLogTag(getClass()), "Connection change");
        context.startService(new Intent(context, UploadService.class));
        if (!Helpers.isTokenSent() && Helpers.checkPlayServices((Activity) context)) {
            // Start IntentService to register this application with GCM.
            Intent gcmIntent = new Intent(context, RegistrationIntentService.class);
            context.startService(gcmIntent);
        }

    }
}
