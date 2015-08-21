package com.byteshaft.kidmonitor;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.byteshaft.kidmonitor.utils.Helpers;

import java.io.File;

public class AppGlobals extends Application {

    public static final int STOPPED_AFTER_TIME = 101;
    public static final int STOPPED_WITH_DIRECT_CALL = 102;
    public static final int SERVER_DIED = 100;
    private static Context sContext;
    private static SharedPreferences sPreferences;
    private static String LOG_TAG = "kid_monitor";
    private static boolean sIsRecordingCall;
    private static boolean sIsVideoRecording;
    private static boolean sIsSoundRecording;

    public static boolean isSoundRecording() {
        return sIsSoundRecording;
    }

    public static void soundRecordingInProgress(boolean value) {
        sIsSoundRecording = value;
    }

    public static boolean isVideoRecording() {
        return sIsVideoRecording;
    }

    public static void videoRecordingInProgress(boolean value) {
        sIsVideoRecording = value;
    }


    public static String getLogTag(Class aClass) {
        return LOG_TAG + aClass.getName();
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getPreferenceManager() {
        return sPreferences;
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
        return getDataDirectory(type)
                + File.separator
                + Helpers.getTimeStamp()
                + Helpers.getFileExtensionForType(type);
    }

    public static boolean isRecordingCall() {
        return sIsRecordingCall;
    }

    public static void setIsRecordingCall(boolean recordingCall) {
        sIsRecordingCall = recordingCall;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
    }
}
