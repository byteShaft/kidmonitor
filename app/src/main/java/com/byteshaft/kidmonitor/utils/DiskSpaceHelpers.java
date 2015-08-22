package com.byteshaft.kidmonitor.utils;

import android.os.Environment;
import android.os.StatFs;

import com.byteshaft.kidmonitor.constants.AppConstants;

import java.util.concurrent.TimeUnit;

public class DiskSpaceHelpers {

    public static boolean isEnoughSpaceForRecording() {
        int bitsPerSecond = getBitRateForResolution(AppConstants.VIDEO_WIDTH,
                AppConstants.VIDEO_HEIGHT);
        long maxTime = TimeUnit.MINUTES.toSeconds(MaxVideoValue());
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

    public static int MaxVideoValue() {
        return AppConstants.MAX_VIDEO_LENGTH;
    }

    private static double getPercentage(double value, int percent) {
        return (value / 100) * percent;
    }
}
