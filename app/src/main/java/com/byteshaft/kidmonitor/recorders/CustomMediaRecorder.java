package com.byteshaft.kidmonitor.recorders;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.byteshaft.kidmonitor.AppGlobals;

import java.util.ArrayList;

public class CustomMediaRecorder extends MediaRecorder implements MediaRecorder.OnInfoListener {

    private String LOG_TAG = AppGlobals.getLogTag(getClass());
    private static boolean sIsRecording;
    private int mDuration;
    private Handler mHandler;
    private static boolean mIsUsable = true;
    private static CustomMediaRecorder mCustomMediaRecorder;
    private ArrayList<OnNewFileWrittenListener> mListeners = new ArrayList<>();
    private ArrayList<OnRecordingStateChangedListener> mStateListeners = new ArrayList<>();
    private String mFilePath;
    private boolean mWasNormalStop;

    public static CustomMediaRecorder getInstance() {
        if (mCustomMediaRecorder == null) {
            mCustomMediaRecorder = new CustomMediaRecorder();
            return mCustomMediaRecorder;
        } else if (!isUsable()) {
            mCustomMediaRecorder = new CustomMediaRecorder();
            return mCustomMediaRecorder;
        } else {
            return mCustomMediaRecorder;
        }
    }

    public void setOnNewFileWrittenListener(OnNewFileWrittenListener listener) {
        mListeners.add(listener);
    }

    public void setOnRecordingStateChangedListener(OnRecordingStateChangedListener listener) {
        mStateListeners.add(listener);
    }

    private CustomMediaRecorder() {
        mHandler = new Handler();
    }

    static boolean isRecording() {
        return sIsRecording;
    }

    private void setIsRecording(boolean state) {
        sIsRecording = state;
    }

    void setDuration(int time) {
        mDuration = time;
    }

    static boolean isUsable() {
        return mIsUsable;
    }

    private void setIsUsable(boolean usable) {
        mIsUsable = usable;
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        if (mDuration > 0) {
            mHandler.postDelayed(stopper, mDuration);
        }
        setIsRecording(true);
        Log.i(LOG_TAG, "Recording Started");
    }

    @Override
    public void stop() {
        super.stop();
        mHandler.removeCallbacks(null);
        setIsRecording(false);
        for (OnNewFileWrittenListener listener : mListeners) {
            listener.onNewRecordingCompleted(mFilePath);
        }
        if (!mWasNormalStop) {
            for (OnRecordingStateChangedListener listener : mStateListeners) {
                listener.onStop(AppGlobals.STOPPED_WITH_DIRECT_CALL, mFilePath);
            }
        } else {
            for (OnRecordingStateChangedListener listener : mStateListeners) {
                listener.onStop(AppGlobals.STOPPED_AFTER_TIME, mFilePath);
            }
        }
        Log.i(LOG_TAG, "Recording Stopped");

    }

    @Override
    public void setOutputFile(String path) throws IllegalStateException {
        super.setOutputFile(path);
        mFilePath = path;
    }

    @Override
    public void reset() {
        super.reset();
        setIsUsable(false);
    }

    private Runnable stopper = new Runnable() {
        @Override
        public void run() {
            if (isRecording()) {
                mWasNormalStop = true;
                stop();
            }
        }
    };

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {
        switch (what) {
            case MEDIA_ERROR_SERVER_DIED:
                for (OnRecordingStateChangedListener listener : mStateListeners) {
                    listener.onStop(AppGlobals.SERVER_DIED, mFilePath);
                }
                break;
        }
    }

    public interface OnNewFileWrittenListener {
        void onNewRecordingCompleted(String path);
    }

    public interface OnRecordingStateChangedListener {
        void onStop(int stopper, String filePath);
    }
}
