package com.byteshaft.kidmonitor.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.byteshaft.kidmonitor.database.MonitorDatabase;
import com.byteshaft.kidmonitor.utils.Helpers;
import com.byteshaft.kidmonitor.utils.SftpHelpers;
import com.byteshaft.kidmonitor.utils.WebServiceHelpers;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadService extends IntentService {

    private static UploadService sInstance;
    private MonitorDatabase mDatabase;

    public UploadService() {
        super("UploadService");
        sInstance = this;
    }

    public static boolean isRunning() {
        return sInstance != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mDatabase = new MonitorDatabase(getApplicationContext());
        if (mDatabase.isEmpty()) {
            mDatabase.close();
            return;
        }

        if (isNetworkAvailable() && isInternetWorking()) {
            ArrayList<HashMap> records = mDatabase.getAllRecords();
            for (HashMap map : records) {
                String type = (String) map.get("data_type");
                if (type.equals("location")) {
                    try {
                        boolean success = WebServiceHelpers.writeLocationLogs(
                                "adgadg", (String) map.get("uri"), (String) map.get("time_stamp"));
                        if (success) {
                            mDatabase.deleteEntry(Integer.valueOf(map.get("unique_id").toString()));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String uri = map.get("uri").toString();
                    File file = new File(uri);
                    if (!file.exists()) {
                        return;
                    }
                    SftpHelpers.upload(type, uri, Integer.valueOf(map.get("unique_id").toString()));
                }
               if (SftpHelpers.mChannelSftp == null) {
                   stopSelf();
                   return;
               }
            }
        }
        mDatabase.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sInstance = null;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("http://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
}
