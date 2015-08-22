package com.byteshaft.kidmonitor.utils;

import android.os.Environment;
import android.os.StatFs;

import com.byteshaft.kidmonitor.constants.AppConstants;

import java.util.concurrent.TimeUnit;

public class DiskSpaceHelpers {

    public static boolean isEnoughSpaceForVideoRecording() {
        int bitsPerSecond = Helpers.getBitRateForResolution(AppConstants.VIDEO_WIDTH,
                AppConstants.VIDEO_HEIGHT);
        long maxTime = TimeUnit.MINUTES.toSeconds(MaxVideoValue());
        double totalBits = bitsPerSecond * maxTime;
        double potentialMegabytesOfRecording = totalBits * 1.25e-7;
        // get ten percent of the potential size.
        double buffer = getPercentage(potentialMegabytesOfRecording, 10);
        return getAvailableSpaceInBits() > potentialMegabytesOfRecording + buffer;
    }

    public static boolean isEnoughSpaceForSoundRecording() {
        long percent = (freeMemory() * 100 / totalMemory());
        return percent >= 10;
    }

    public static long totalMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long total = ((long)statFs.getBlockCount() * (long)statFs.getBlockSize()) / 1048576;
        return total;
    }

    public static long freeMemory() {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        long free  = ((long)statFs.getAvailableBlocks() * (long)statFs.getBlockSize()) / 1048576;
        return free;
    }

    private static double getAvailableSpaceInBits() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return stat.getAvailableBlocks() * (double) stat.getBlockSize();
    }

    public static int MaxVideoValue() {
        return AppConstants.MAX_VIDEO_LENGTH;
    }

    private static double getPercentage(double value, int percent) {
        return (value / 100) * percent;
    }
}
