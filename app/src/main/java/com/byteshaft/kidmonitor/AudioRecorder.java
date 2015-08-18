package com.byteshaft.kidmonitor;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class AudioRecorder extends MediaRecorder {

    public void record(int time) {
        setAudioSource(MediaRecorder.AudioSource.MIC);
        setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            prepare();
        }catch (IOException e) {
            Log.e("AudioRecorder", "Could not record audio.");
        }
        start();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stop();
            }
        }, time);

    }

    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        reset();
        release();
    }
}
