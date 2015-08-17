package com.byteshaft.kidmonitor;


import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;

import java.io.IOException;

public class VideoRecorder implements CameraStateChangeListener {

    private MediaRecorder mMediaRecorder;
    private static boolean sIsRecording;
    private Flashlight flashlight;

    void start(android.hardware.Camera camera, SurfaceHolder holder) {
        camera.unlock();
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setCamera(camera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setPreviewDisplay(holder.getSurface());
        String path = AppGlobals.getDataDirectory("videos") + "/test.mp4";
        System.out.println(path);
        mMediaRecorder.setOutputFile(path);
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
        }, 5000);
    }

    public void start() {
        flashlight = new Flashlight(AppGlobals.getContext());
        flashlight.setCameraStateChangedListener(this);
        flashlight.setupCameraPreview();
        sIsRecording = true;
    }

    void stopRecording() {
        Silencer.silentSystemStream(2000);
        mMediaRecorder.stop();
        flashlight.releaseAllResources();
        mMediaRecorder.release();
        sIsRecording = false;
    }

    @Override
    public void onCameraInitialized() {

    }

    @Override
    public void onCameraViewSetup(Camera camera, SurfaceHolder surfaceHolder) {
        start(camera, surfaceHolder);
    }

    @Override
    public void onCameraBusy() {

    }
    public static boolean isRecording() {
        return sIsRecording;
    }
}
