package com.byteshaft.kidmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class IncomingCallStateListener extends PhoneStateListener {

    Context mContext;

    public IncomingCallStateListener(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                System.out.println("RINGING");
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                System.out.println("IDLE");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                System.out.println("OFFHOOK");
                break;
        }
    }

    BroadcastReceiver mOutgoingCallListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String outGoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            System.out.println(outGoingNumber);
        }
    };
}
