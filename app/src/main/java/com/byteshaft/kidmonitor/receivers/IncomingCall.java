package com.byteshaft.kidmonitor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.byteshaft.kidmonitor.utils.Helpers;

public class IncomingCall extends BroadcastReceiver {

    private static IncomingCallStateListener sIncomingCallStateListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (sIncomingCallStateListener == null) {
            Log.v("CALL", "Registering CallStateListener");
            sIncomingCallStateListener = new IncomingCallStateListener();
            TelephonyManager telephonyManager = Helpers.getTelephonyManager();
            telephonyManager.listen(
                    sIncomingCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}
