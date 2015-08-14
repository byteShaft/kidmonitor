package com.byteshaft.kidmonitor;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;


public class LocationService extends ContextWrapper implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

        GoogleApiClient mGoogleApiClient;
        private int mLocationRecursionCounter = 0;
        private int mLocationChangedCounter = 0;
        private LocationRequest mLocationRequest;
        public Location mLocation;
        String lat;
        String lon;

        public LocationService(Context context) {
                super(context);
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
                mLocationChangedCounter++;
                if (mLocationChangedCounter == 3) {
                        mLocation = location;
                        String lat = String.valueOf(mLocation.getLatitude());
                        String lon = String.valueOf(mLocation.getLongitude());
                        Log.i("Tatti", lat + ", " + lon);
                        stopLocationService();
                }
                Log.i("TrackBuddy", "onLocationChanged CALLED..." + mLocationChangedCounter);

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }
}
