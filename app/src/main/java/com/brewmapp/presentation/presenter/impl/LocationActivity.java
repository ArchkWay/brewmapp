package com.brewmapp.presentation.presenter.impl;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by oleg on 30.09.17.
 */

public abstract class LocationActivity extends BaseActivity {

    private FusedLocationProviderClient client;
    private boolean oneShot;
    private LocationRequest locationRequest = new LocationRequest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    protected void getDeviceLocation() {
        try {
            Task<Location> locationResult = client.getLastLocation();
            locationResult.addOnCompleteListener(this, listener);
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private OnCompleteListener<Location> listener = task -> {
        if (task.isSuccessful()) {
            onLocationFound(task.getResult());
        } else {
            try {
                showMessage(task.getException().getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(e.getMessage());
            }
        }
    };

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            onLocationFound(locationResult.getLastLocation());
            if(oneShot) {
                client.removeLocationUpdates(this);
            }
        }
    };

    protected void lookForLocation(boolean oneShot) {
        try {
            this.oneShot = oneShot;
            client.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected abstract void onLocationFound(Location location);
}
