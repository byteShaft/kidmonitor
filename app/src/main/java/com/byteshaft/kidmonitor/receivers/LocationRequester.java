package com.byteshaft.kidmonitor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.kidmonitor.constants.AppConstants;
import com.byteshaft.kidmonitor.utils.RemoteCallsHelpers;

public class LocationRequester extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case AppConstants.REQUEST_LOCATION:
                RemoteCallsHelpers.requestLocationLogging();
                break;
            case AppConstants.REQUEST_AUDIO:
                RemoteCallsHelpers.requestAudioRecording(intent.getExtras().getInt("duration"));
                break;
            case AppConstants.REQUEST_VIDEO:
                RemoteCallsHelpers.requestVideoRecording(intent.getExtras().getInt("duration"));
                break;
        }
    }
}
