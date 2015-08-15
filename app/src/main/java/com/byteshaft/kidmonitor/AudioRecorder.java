package com.byteshaft.kidmonitor;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class AudioRecorder {

    MediaRecorder mediaRecorder;

    public void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(AppGlobals.getDataDirectory("callrec") + "test.aac");
        try {
            mediaRecorder.prepare();
        }catch (IOException e) {
            Log.e("Error", "Tatti occurred");
        }
        mediaRecorder.start();

    }

    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
    }

}
