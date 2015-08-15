package com.byteshaft.kidmonitor;


import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;

import java.io.IOException;

public class VideoRecorder implements CameraStateChangeListener {

    private MediaRecorder mediaRecorder;
    private boolean isRecrding = false;
    private Flashlight flashlight;

    void start(android.hardware.Camera camera, SurfaceHolder holder) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mediaRecorder.setPreviewDisplay(holder.getSurface());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Context context) {
        flashlight = new Flashlight(context);
        flashlight.setCameraStateChangedListener(this);
        flashlight.setupCameraPreview();
    }

    void stopRecording() {
        mediaRecorder.stop();
        flashlight.releaseAllResources();
        mediaRecorder.release();

        isRecrding = false;
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
}
