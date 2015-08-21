package com.byteshaft.kidmonitor.recorders;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.database.MonitorDatabase;
import com.byteshaft.kidmonitor.utils.Helpers;

import java.io.IOException;
import java.util.ArrayList;

public class AudioRecorder extends MediaRecorder {

    private static AudioRecorder instance;

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
    }

    public void record(String recordingType) {
        mRecordType = recordingType;
        mOutputFilePath = AppGlobals.getNewFilePathForType(recordingType);
        setAudioSource(MediaRecorder.AudioSource.MIC);
        setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        setOutputFile(mOutputFilePath);
        try {
            prepare();
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
        mRecordTime = time;
        record(recordingType);
    }

    private Runnable getStopRecordingRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                stop();
            }
        };
    }

    public interface OnAudioRecorderStateChangedListener {
        void onStart();

        void onStop(String outputFile);
    }
}
