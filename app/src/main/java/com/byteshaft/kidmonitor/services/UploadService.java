package com.byteshaft.kidmonitor.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.byteshaft.kidmonitor.database.MonitorDatabase;
import com.byteshaft.kidmonitor.utils.SftpHelpers;
import com.byteshaft.kidmonitor.utils.WebServiceHelpers;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class UploadService extends IntentService {

    private static UploadService sInstance;

    public UploadService() {
        super("UploadService");
        sInstance = this;
    }

    public static boolean isRunning() {
        return sInstance != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MonitorDatabase database = new MonitorDatabase(getApplicationContext());
        if (database.isEmpty()) {
            database.close();
            return;
        }

        if (isNetworkAvailable() && isInternetWorking()) {
            ArrayList<HashMap> records = database.getAllRecords();
            for (HashMap map : records) {
                String type = (String) map.get("data_type");
                if (type.equals("location")) {
                    try {
                        boolean success = WebServiceHelpers.writeLocationLogs(
                                "adgadg", (String) map.get("uri"), (String) map.get("time_stamp"));
                        if (success) {
                            database.deleteEntry(Integer.valueOf(map.get("unique_id").toString()));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        SftpHelpers.upload(type, (String) map.get("uri"));
                        database.deleteEntry(Integer.valueOf(map.get("unique_id").toString()));
                    } catch (JSchException | SftpException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        database.close();
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
