package com.byteshaft.kidmonitor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    LocationService mLocationService;
    String lat;
    String lon;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationService = new LocationService(getApplicationContext());
        Button locationButton = (Button) findViewById(R.id.button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationService.connectingGoogleApiClient();
                if (mLocationService.mGoogleApiClient.isConnected()) {
                    mLocationService.startLocationUpdates();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationService.mGoogleApiClient.isConnected()) {
            mLocationService.stopLocationService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationService.mGoogleApiClient.isConnected()) {
            mLocationService.stopLocationService();
        }
    }
}
