package com.byteshaft.kidmonitor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.services.UploadService;

public class ConnectionChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(AppGlobals.getLogTag(getClass()), "Connection change");
        context.startService(new Intent(context, UploadService.class));

    }
}
