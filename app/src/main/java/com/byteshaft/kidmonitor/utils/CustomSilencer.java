package com.byteshaft.kidmonitor.utils;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

import com.byteshaft.kidmonitor.AppGlobals;

public class CustomSilencer {

    private static AudioManager audioManager = (AudioManager)
            AppGlobals.getContext().getSystemService(Context.AUDIO_SERVICE);

    private static int music;
    private static int alarm;
    private static int dtmf;
    private static int ring;
    private static int system;
    private static int voice;

    private static void backupVolumes() {
        music = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        alarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        dtmf = audioManager.getStreamVolume(AudioManager.STREAM_DTMF);
        ring = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        system = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        voice = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
    }

    private static void silentAll() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_DTMF, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, 0, 0);
    }

    private static void restoreStreams() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, music, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, alarm, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_DTMF, dtmf, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, ring, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, system, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, voice, 0);
    }

    public static void silentAllStreams(int time) {
        backupVolumes();
        silentAll();
        // Restore after 2 seconds.
        new Handler().postDelayed(silencer, time);
    }

    private static Runnable silencer = new Runnable() {
        @Override
        public void run() {
            restoreStreams();
        }
    };

}
