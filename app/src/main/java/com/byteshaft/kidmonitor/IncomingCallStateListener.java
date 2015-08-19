package com.byteshaft.kidmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class IncomingCallStateListener extends PhoneStateListener {

    private Context mContext;
    private AudioRecorder recorder;
    private static boolean sIsRecording = false;
    static String sInCommingNumber = null;


    public IncomingCallStateListener(Context context) {
        super();
        mContext = context;
        recorder = new AudioRecorder();
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        sInCommingNumber = incomingNumber;
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                System.out.println("RINGING");
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                System.out.println("IDLE");
                if (sIsRecording) {
                    recorder.stopMediaRecorder();
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                recorder = new AudioRecorder();
                recorder.record();
                sIsRecording = true;
                System.out.println("OK");
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
