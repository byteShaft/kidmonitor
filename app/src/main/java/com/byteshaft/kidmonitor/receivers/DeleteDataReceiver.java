package com.byteshaft.kidmonitor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.byteshaft.kidmonitor.database.MonitorDatabase;

import java.io.File;
import java.io.IOException;


public class DeleteDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", -1);
        String url = intent.getStringExtra("url");
        if (id != -1) {
            MonitorDatabase database = new MonitorDatabase(context);
            database.deleteEntry(id);

        }
        if (!url.isEmpty()) {
            removeFiles(url);
        }

    }

    void removeFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
