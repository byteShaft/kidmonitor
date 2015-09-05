package com.byteshaft.kidmonitor.recorders;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.database.MonitorDatabase;
import com.byteshaft.kidmonitor.utils.Helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioRecorder extends MediaRecorder {

    private static AudioRecorder instance;
    public static final int SAMPLING_RATE = 44100;

    private ArrayList<OnAudioRecorderStateChangedListener> mListeners = new ArrayList<>();
    private String mOutputFilePath;
    private int mRecordTime;
    private String mRecordType;

    public static AudioRecorder getInstance() {
        if (instance == null) {
            instance = new AudioRecorder();
        }
        return instance;
    }

    public void setOnRecordStateChangedListener(OnAudioRecorderStateChangedListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        for (OnAudioRecorderStateChangedListener listener : mListeners) {
            listener.onStart();
        }
    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        reset();
        release();
        for (OnAudioRecorderStateChangedListener listener : mListeners) {
            listener.onStop(mOutputFilePath);
        }
        MonitorDatabase database = new MonitorDatabase(AppGlobals.getContext());
        database.createNewEntry(mRecordType, mOutputFilePath, Helpers.getTimeStamp());
        instance = null;
        AppGlobals.soundRecordingInProgress(false);
    }

    public void record(String recordingType) {
        mRecordType = recordingType;
        mOutputFilePath = AppGlobals.getNewFilePathForType(recordingType);
        setAudioSource(AudioSource.VOICE_COMMUNICATION);
        setAudioEncodingBitRate(96000);
        setAudioSamplingRate(SAMPLING_RATE);
        setOutputFormat(OutputFormat.DEFAULT);
        setOutputFile(mOutputFilePath);
        setAudioEncoder(AudioEncoder.AAC);
        try {
            prepare();
            System.out.println("Recording for " + mRecordTime);
            start();
            Log.i(AppGlobals.getLogTag(getClass()), "Recording started !...");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stop recording automatically after the requested time.
        if (mRecordTime > 0) {
            new Handler().postDelayed(getStopRecordingRunnable(), mRecordTime);
        }
    }

    public void record(String recordingType, int time) {
        AppGlobals.soundRecordingInProgress(true);
        if (time > TimeUnit.MINUTES.toMillis(15)) {
            mRecordTime = (int) TimeUnit.MINUTES.toMillis(15);
        } else {
            mRecordTime = time;
        }
        if (!AppGlobals.isVideoRecording()) {
            record(recordingType);
        }
    }

    private Runnable getStopRecordingRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("Stopped");
                stop();
            }
        };
    }

    public interface OnAudioRecorderStateChangedListener {
        void onStart();

        void onStop(String outputFile);
    }
}
