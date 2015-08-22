package com.byteshaft.kidmonitor.utils;


import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;

import java.util.concurrent.TimeUnit;

public class DIskSpaceHelpers {

    public static boolean isEnoughSpaceForRecording() {
        int videoWidth = 640;
        int videoHeight = 480;
        int bitsPerSecond = getBitRateForResolution(videoWidth, videoHeight);
        long maxTime = TimeUnit.MINUTES.toSeconds(readMaxVideoValue());
        double totalBits = bitsPerSecond * maxTime;
        double potentialMegabytesOfRecording = totalBits * 1.25e-7;
        // get ten percent of the potential size.
        double buffer = getPercentage(potentialMegabytesOfRecording, 10);
        System.out.println(getAvailableSpaceInBits() > potentialMegabytesOfRecording + buffer);
        return getAvailableSpaceInBits() > potentialMegabytesOfRecording + buffer;
    }

    private static double getAvailableSpaceInBits() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return stat.getAvailableBlocks() * (double) stat.getBlockSize();
    }

    public static int getBitRateForResolution(int width, int height) {
        // Not perfect but gets use there.
        return (width * height) * 6;
    }

    public static int readMaxVideoValue() {
        return 15;
    }

    private static double getPercentage(double value, int percent) {
        return (value / 100) * percent;
    }
}
