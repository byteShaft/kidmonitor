package com.byteshaft.kidmonitor;


import android.content.Context;
import android.content.ContextWrapper;
import android.telephony.TelephonyManager;

public class Helpers extends ContextWrapper {

    public Helpers(Context base) {
        super(base);
    }

    TelephonyManager getTelephonyManager() {
        return (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
    }
}
