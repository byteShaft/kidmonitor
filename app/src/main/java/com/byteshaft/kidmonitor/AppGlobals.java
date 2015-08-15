package com.byteshaft.kidmonitor;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import java.io.File;

public class AppGlobals extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

    public static String getDataDirectory(String type) {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dataDirectory = sdcard + "/Android/data/";
        String directoryPath = dataDirectory + sContext.getPackageName() + "/" + type;
        File file = new File(directoryPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }
}
