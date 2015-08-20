package com.byteshaft.kidmonitor.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.telephony.TelephonyManager;

import com.byteshaft.kidmonitor.AppGlobals;
import com.byteshaft.kidmonitor.constants.AppConstants;
import com.byteshaft.kidmonitor.services.UploadService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Helpers {

    private static Context context = AppGlobals.getContext();

    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static boolean isAnyLocationServiceAvailable() {
        LocationManager locationManager = getLocationManager();
        return isGpsEnabled(locationManager) || isNetworkBasedGpsEnabled(locationManager);
    }

    private static LocationManager getLocationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    static boolean isGpsEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private static boolean isNetworkBasedGpsEnabled(LocationManager locationManager) {
        return locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER));
    }

    public static String getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getFileExtensionForType(String contentType) {
        switch (contentType) {
            case AppConstants.TYPE_SOUND_RECORDINGS:
            case AppConstants.TYPE_CALL_RECORDINGS:
                return ".aac";
            case AppConstants.TYPE_VIDEO_RECORDINGS:
                return ".mp4";
            default:
                return ".unknown";
        }
    }

    public static String getCurrentDateandTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyy h:mm a zz");
        return sdf.format(new Date());
    }

    public static void checkInternetAndUploadPendingData() {
        Context context = AppGlobals.getContext();
        context.startService(new Intent(context, UploadService.class));
    }
}