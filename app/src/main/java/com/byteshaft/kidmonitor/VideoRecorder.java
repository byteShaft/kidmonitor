package com.byteshaft.kidmonitor;


import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;

import java.io.IOException;

public class VideoRecorder implements CameraStateChangeListener {

    private MediaRecorder mediaRecorder;
    private static boolean isRecording;
    private Flashlight flashlight;

    void start(android.hardware.Camera camera, SurfaceHolder holder) {
        camera.unlock();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
        mediaRecorder.setVideoSize(640, 480);
        mediaRecorder.setPreviewDisplay(holder.getSurface());
        String path = AppGlobals.getDataDirectory("videos") + "/test.mp4";
        System.out.println(path);
        mediaRecorder.setOutputFile(path);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        flashlight = new Flashlight(AppGlobals.getContext());
        flashlight.setCameraStateChangedListener(this);
        flashlight.setupCameraPreview();
        isRecording = true;
    }

    void stopRecording() {
        mediaRecorder.stop();
        flashlight.releaseAllResources();
        mediaRecorder.release();
        isRecording = false;
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
        return isRecording;
    }
}
