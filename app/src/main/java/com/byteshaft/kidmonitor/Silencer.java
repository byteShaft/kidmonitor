package com.byteshaft.kidmonitor;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

public class Silencer {

    private static int sVolumeLevelBackup;
    private static boolean sInProgress;
    private static AudioManager sAudioManager;
    private static Handler sHandler;
    private static Runnable sRestorer;

    public static synchronized void silentSystemStream(int millis) {
        AudioManager manager = getAudioManager();
        Handler handler;
        if (sInProgress) {
            handler = getHandler();
            handler.removeCallbacks(getRestoreRunnable(manager));
        }
        backupVolumeLevel(manager);
        silentSystemStream(manager);
        sInProgress = true;
        restoreVolumeAfter(manager, millis);
    }

    private static Runnable getRestoreRunnable(final AudioManager manager) {
        if (sRestorer == null) {
            sRestorer = new Runnable() {
                @Override
                public void run() {
                    manager.setStreamVolume(AudioManager.STREAM_SYSTEM, sVolumeLevelBackup, 0);
                    sInProgress = false;
                }
            };
        }
        return sRestorer;
    }

    private static AudioManager getAudioManager() {
        Context context = AppGlobals.getContext();
        if (sAudioManager == null) {
            sAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        return sAudioManager;
    }

    private static void backupVolumeLevel(AudioManager manager) {
        if (!sInProgress) {
            sVolumeLevelBackup = manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        }
    }

    private static void silentSystemStream(AudioManager manager) {
        manager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
    }

    private static Handler getHandler() {
        if (sHandler == null) {
            sHandler = new Handler();
        }
        return sHandler;
    }

    private static void restoreVolumeAfter(AudioManager manager, int time) {
        Handler handler = getHandler();
        handler.postDelayed(getRestoreRunnable(manager), time);
    }
}
