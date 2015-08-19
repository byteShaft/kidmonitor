package com.byteshaft.kidmonitor;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.byteshaft.kidmonitor.database.DataBaseHelpers;
import com.byteshaft.kidmonitor.database.LocationDataBaseConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;

public class LocationService extends ContextWrapper implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , DataBaseHelpers.OnDatabaseChangedListener {

    GoogleApiClient mGoogleApiClient;
    private int mLocationChangedCounter = 0;
    private LocationRequest mLocationRequest;
    public Location mLocation;
    private CountDownTimer mTimer;
    private DataBaseHelpers dataBaseHelpers;


    public LocationService(Context context) {
        super(context);
        dataBaseHelpers = new DataBaseHelpers(context);
    }

    public void connectingGoogleApiClient() {
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        long INTERVAL = 0;
        long FASTEST_INTERVAL = 0;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationService() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        locationTimer().cancel();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("KidMonitor", "onLocationChanged CALLED..." + mLocationChangedCounter);
        mLocationChangedCounter++;
        if (mLocationChangedCounter == 3) {
            mLocation = location;
            String lat = String.valueOf(mLocation.getLatitude());
            String lon = String.valueOf(mLocation.getLongitude());
            String googleMapsLink = "https://maps.google.com/maps?q=" + lat + "," + lon;
            // save to database if Internet Not available
            dataBaseHelpers.newEntryToDatabase(LocationDataBaseConstants.UPLOAD_LOCATION_COLUMN,
                    googleMapsLink,LocationDataBaseConstants.TABLE_NAME);
            dataBaseHelpers.setOnDatabaseChangedListener(this);
            Log.i("Location", lat + ", " + lon);
                    /* TODO: Implement Location Response */
            stopLocationService();
            mLocationChangedCounter = 0;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public CountDownTimer locationTimer() {

        if (mTimer == null) {
            mTimer = new CountDownTimer(120000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("Location", "Timer: " + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    if (mGoogleApiClient.isConnected()) {
                        stopLocationService();
                        Log.i("Location", "Location cannot be acquired.");
                            /* TODO: Implement Response */
                    }
                }
            };
        }
        return mTimer;
    }

    @Override
    public void onNewEntryCreated() {
        System.out.println("OK");
    }
}
