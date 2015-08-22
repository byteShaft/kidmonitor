package com.byteshaft.kidmonitor.utils;

import android.content.SharedPreferences;
import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.constants.AppConstants;
import com.byteshaft.kidmonitor.recorders.AudioRecorder;
import com.byteshaft.kidmonitor.recorders.VideoRecorder;
import com.byteshaft.kidmonitor.services.LocationService;

public class RemoteCallsHelpers {

    public static boolean isCallRecordingEnabled() {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        return preferences.getBoolean("call_recording", true);
    }

    public static void enableCallRecording(boolean enable) {
        SharedPreferences preferences = AppGlobals.getPreferenceManager();
        preferences.edit().putBoolean("call_recording", enable).apply();
    }

    public static void requestLocationLogging() {
        LocationService locationService;
        locationService = new LocationService(AppGlobals.getContext());
        if (Helpers.isAnyLocationServiceAvailable()) {
            locationService.connectingGoogleApiClient();
            locationService.locationTimer().start();
        }
    }

    public static void requestAudioRecording(int duration) {
        AudioRecorder audioRecorder = new AudioRecorder();
        audioRecorder.record(AppConstants.TYPE_SOUND_RECORDINGS, duration);
    }

    public static void requestVideoRecording(int duration) {
        if (DIskSpaceHelpers.isEnoughSpaceForRecording()) {
            VideoRecorder videoRecorder = new VideoRecorder();
            videoRecorder.start(duration);
        }
    }
}
