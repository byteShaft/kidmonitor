package com.byteshaft.kidmonitor.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

import com.byteshaft.kidmonitor.AppGlobals;

public class CustomSilencer {

    private static AudioManager audioManager = (AudioManager)
            AppGlobals.getContext().getSystemService(Context.AUDIO_SERVICE);

    private static int system;

    private static void backupVolumes() {
        system = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }

    private static void silentAll() {
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
    }

    private static void restoreStreams() {
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, system, 0);
    }

    public static void silentAllStreams(int time) {
        backupVolumes();
        silentAll();
        new Handler().postDelayed(silencer, time);
    }

    private static Runnable silencer = new Runnable() {
        @Override
        public void run() {
            restoreStreams();
        }
    };

}
