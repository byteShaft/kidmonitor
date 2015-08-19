package com.byteshaft.kidmonitor;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.byteshaft.kidmonitor.utils.Helpers;

import java.io.File;

public class AppGlobals extends Application {

    private static Context sContext;
    private static String LOG_TAG = "kid_monitor";
    public static final int STOPPED_AFTER_TIME = 101;
    public static final int STOPPED_WITH_DIRECT_CALL = 102;
    public static final int SERVER_DIED = 100;
    private static boolean mIsRecordingCall;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static String getLogTag(Class aClass) {
        return LOG_TAG + aClass.getName();
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getDataDirectory(String type) {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dataDirectory = sdcard + "/Android/data/";
        String directoryPath = dataDirectory
                + sContext.getPackageName()
                + File.separator
                + type
                + File.separator;
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getNewFilePathForType(String type) {
        return getDataDirectory(type) + File.separator + Helpers.getTimeStamp();
    }

    public static boolean isRecordingCall() {
        return mIsRecordingCall;
    }

    public static void setIsRecordingCall(boolean recordingCall) {
        mIsRecordingCall = recordingCall;
    }
}
