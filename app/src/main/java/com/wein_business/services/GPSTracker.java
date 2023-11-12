package com.wein_business.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;

import com.wein_business.R;
import com.wein_business.ui.activity.generic.GenericActivity;

public class GPSTracker extends Service {

    private GenericActivity activity;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    private static final float MIN_DISTANCE_UPDATE = 10.0f; // 10 meters
    private static final long MIN_TIME_UPDATE = 1000 * 5; // 30 seconds
    protected LocationManager locationManager;

    private Location location;

    public GPSTracker(GenericActivity activity) {
        this.activity = activity;
        locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
    }

    public boolean isLocationEnabled() {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            showSettingsAlert();
            return false;
        } else
            return true;
    }

    @SuppressLint("MissingPermission")
    public Location getCurrentLocation() {
        location = null;
        try {
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                showSettingsAlert();
                return location;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location == null && isNetworkEnabled) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(activity.getResources().getString(R.string.gps_settings));
        alertDialog.setMessage(activity.getResources().getString(R.string.gps_settings_text));
        alertDialog.setPositiveButton(activity.getResources().getString(R.string.open_settings), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }
}