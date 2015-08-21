package com.byteshaft.kidmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.byteshaft.kidmonitor.recorders.VideoRecorder;
import com.byteshaft.kidmonitor.services.CallListenerService;
import com.byteshaft.kidmonitor.services.LocationService;
import com.byteshaft.kidmonitor.services.RegistrationIntentService;
import com.byteshaft.kidmonitor.utils.Helpers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {

    public LocationService mLocationService;
    private VideoRecorder videoRecorder;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button locationButton = (Button) findViewById(R.id.button_location);
        Button soundRec = (Button) findViewById(R.id.button_sound);
        Button videoButton = (Button) findViewById(R.id.button_video);
        soundRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        videoRecorder = new VideoRecorder();
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VideoRecorder.isRecording()) {
                    videoRecorder.start();
                    Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_SHORT).show();
                } else if (VideoRecorder.isRecording()) {
                    videoRecorder.stopRecording();
                    Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT).show();
                }

            }
        });
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationService = new LocationService(getApplicationContext());
                if (!Helpers.isAnyLocationServiceAvailable()) {
                    Log.i("Location", "GPS disabled");
                    /* TODO: Implement Response */
                } else {
                    mLocationService.connectingGoogleApiClient();
                    mLocationService.locationTimer().start();
                }
            }
        });
        startService(new Intent(this, CallListenerService.class));

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Registered");
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("registrationComplete"));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        if (mLocationService != null && mLocationService.mGoogleApiClient.isConnected()) {
            mLocationService.stopLocationService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationService != null && mLocationService.mGoogleApiClient.isConnected()) {
            mLocationService.stopLocationService();
        }
    }
}