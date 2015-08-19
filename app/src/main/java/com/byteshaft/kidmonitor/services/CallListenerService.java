package com.byteshaft.kidmonitor.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.byteshaft.kidmonitor.receivers.IncomingCallStateListener;
import com.byteshaft.kidmonitor.utils.Helpers;

public class CallListenerService extends Service {

    private IncomingCallStateListener mIncomingCallStateListener;
    private TelephonyManager mTelephonyManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIncomingCallStateListener = new IncomingCallStateListener();
        mTelephonyManager = Helpers.getTelephonyManager();
        mTelephonyManager.listen(mIncomingCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTelephonyManager.listen(mIncomingCallStateListener, PhoneStateListener.LISTEN_NONE);
    }
}
