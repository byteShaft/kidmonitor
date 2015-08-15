package com.byteshaft.kidmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;

public class IncomingCallStateListener extends PhoneStateListener {

    Context mContext;

    BroadcastReceiver mOutgoingCallListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Outgoing");
        }
    };

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        System.out.println("Incoming");
    }

    public IncomingCallStateListener(Context context) {
        super();
        mContext = context;
    }
}
