package com.byteshaft.kidmonitor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.kidmonitor.services.CallListenerService;


public class BootStateListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, CallListenerService.class));
    }
}
