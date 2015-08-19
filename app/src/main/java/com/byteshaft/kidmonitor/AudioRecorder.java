package com.byteshaft.kidmonitor;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class AudioRecorder implements AudioCustomMediaRecorder.PlaybackStateChangedListener {

    AudioCustomMediaRecorder audioCustomMediaRecorder;

    public void record() {
        Helpers helpers = new Helpers(AppGlobals.getContext());
        audioCustomMediaRecorder = new AudioCustomMediaRecorder();
        String path = AppGlobals.getDataDirectory("callrec") +  "/" + helpers.getTimeStamp()+
                "_"+IncomingCallStateListener.sInCommingNumber + ".aac";
        audioCustomMediaRecorder.reset();
        audioCustomMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioCustomMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        audioCustomMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        audioCustomMediaRecorder.setOutputFile(path);
        audioCustomMediaRecorder.setOnPlaybackStateChangedListener(this);
        // STREAM TO PHP? //Alert
        try {
            audioCustomMediaRecorder.prepare();
        } catch (java.io.IOException e) {
            return;
        }
        audioCustomMediaRecorder.start();
    }

    void stopMediaRecorder() {
        audioCustomMediaRecorder.stop();
        audioCustomMediaRecorder.reset();
        audioCustomMediaRecorder.release();
    }

    @Override
    public void onStop(String outputFilePath) {
        System.out.println("OUTPUT"+outputFilePath);
        if (IncomingCallStateListener.sInCommingNumber != null) {
            IncomingCallStateListener.sInCommingNumber = null;
        }
    }

    @Override
    public void onStart() {

    }
}
