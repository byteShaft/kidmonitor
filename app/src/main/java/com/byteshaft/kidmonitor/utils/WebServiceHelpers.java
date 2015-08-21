package com.byteshaft.kidmonitor.utils;

import org.json.JSONException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebServiceHelpers {

    private static final String LOCATION_UPLOAD_URL = "http://128.199.125.71/locations.json";

    public static boolean writeLocationLogs(String deviceID, String uri, String timeStamp) throws
            IOException, JSONException {

        String data = getJsonObjectString(deviceID, uri, timeStamp);
        HttpURLConnection connection = openConnectionForUrl(LOCATION_UPLOAD_URL);
        connection.connect();
        sendRequestData(connection, data);
        return connection.getResponseCode() == 201;
    }

    private static HttpURLConnection openConnectionForUrl(String path)
            throws IOException {

        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        return connection;
    }

    private static String getJsonObjectString(String deviceID, String uri, String timeStamp) {

        return String.format(
                "{\"location\": { \"url\": \"%s\", \"device_id\": \"%s\", \"remote_created_at\": \"%s\"} }", uri, deviceID, timeStamp
        );
    }

    private static void sendRequestData(HttpURLConnection connection, String body)
            throws IOException {

        byte[] outputInBytes = body.getBytes("UTF-8");
        OutputStream os = connection.getOutputStream();
        os.write(outputInBytes);
        os.close();
    }
}
