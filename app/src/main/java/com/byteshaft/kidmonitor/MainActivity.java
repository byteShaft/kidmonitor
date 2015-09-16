package com.byteshaft.kidmonitor;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.byteshaft.kidmonitor.services.RegistrationIntentService;
import com.byteshaft.kidmonitor.utils.Helpers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Helpers.isTokenSent() && Helpers.checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent gcmIntent = new Intent(this, RegistrationIntentService.class);
            startService(gcmIntent);
        }

        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(getApplicationContext(),
                com.byteshaft.kidmonitor.MainActivity.class);
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        finish();
    }
}