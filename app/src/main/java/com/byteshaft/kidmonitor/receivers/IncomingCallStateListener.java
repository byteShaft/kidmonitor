package com.byteshaft.kidmonitor.receivers;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.constants.AppConstants;
import com.byteshaft.kidmonitor.recorders.AudioRecorder;

public class IncomingCallStateListener extends PhoneStateListener {

    private AudioRecorder recorder;

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                if (AppGlobals.isRecordingCall()) {
                    recorder.stop();
                    AppGlobals.setIsRecordingCall(false);
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (!AppGlobals.isRecordingCall()) {
                    recorder = AudioRecorder.getInstance();
                    recorder.record(AppConstants.TYPE_CALL_RECORDINGS);
                    AppGlobals.setIsRecordingCall(true);
                }
                break;
        }
    }
}
