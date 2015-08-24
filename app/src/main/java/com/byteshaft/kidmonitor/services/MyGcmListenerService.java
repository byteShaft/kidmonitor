package com.byteshaft.kidmonitor.services;


import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        System.out.println(data.getString("action"));
        System.out.println(data.getString("state"));

    }
}
