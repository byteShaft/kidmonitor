package com.byteshaft.kidmonitor;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallListenerService extends Service {

    private IncomingCallStateListener mIncomingCallStateListener;
    private TelephonyManager mTelephonyManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Helpers helpers = new Helpers(getApplicationContext());
        mIncomingCallStateListener = new IncomingCallStateListener(getApplicationContext());
        mTelephonyManager = helpers.getTelephonyManager();
        mTelephonyManager.listen(mIncomingCallStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mIncomingCallStateListener.mOutgoingCallListener, intentFilter);
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
        unregisterReceiver(mIncomingCallStateListener.mOutgoingCallListener);
    }
}
