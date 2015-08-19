package com.byteshaft.kidmonitor;


import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;

import com.byteshaft.ezflashlight.CameraStateChangeListener;
import com.byteshaft.ezflashlight.Flashlight;
import com.byteshaft.kidmonitor.database.DataBaseHelpers;
import com.byteshaft.kidmonitor.database.VideoRecordingdataBaseConstants;

import java.io.IOException;

public class VideoRecorder implements CameraStateChangeListener,
        CustomMediaRecorder.OnNewFileWrittenListener,
        CustomMediaRecorder.OnRecordingStateChangedListener {

    private CustomMediaRecorder mMediaRecorder;
    private static boolean sIsRecording;
    private Flashlight flashlight;
    private Helpers mHelpers;
    private DataBaseHelpers mDataBaseHelpers;

    public VideoRecorder() {
        mDataBaseHelpers = new DataBaseHelpers(AppGlobals.getContext());

    }

    void start(android.hardware.Camera camera, SurfaceHolder holder, int time) {
        camera.unlock();
        mMediaRecorder = CustomMediaRecorder.getInstance();
        mHelpers = new Helpers(AppGlobals.getContext());
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
        String path = AppGlobals.getDataDirectory("videos") + "/" + mHelpers.getTimeStamp() +".mp4";
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
        },time);
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
        start(camera, surfaceHolder, 5000);
    }

    @Override
    public void onCameraBusy() {

    }
    public static boolean isRecording() {
        return sIsRecording;
    }

    @Override
    public void onNewRecordingCompleted(String path) {
        if (mHelpers.isNetworkAvailable()) {

        } else {
            mDataBaseHelpers.newEntryToDatabase(VideoRecordingdataBaseConstants.UPLOAD_VIDEO_RECORDING
                    , path, VideoRecordingdataBaseConstants.TABLE_NAME);

        }
    }

    @Override
    public void onStop(int stopper, String filePath) {

    }
}
