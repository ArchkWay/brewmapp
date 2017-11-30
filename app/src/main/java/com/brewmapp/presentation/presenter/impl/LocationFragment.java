package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nlbochas on 21/10/2017.
 */

public abstract class LocationFragment extends BaseFragment {

    private FusedLocationProviderClient client;
    private LocationRequest locationRequest = new LocationRequest();

    @Override
    public void onAttach(Context context) {
        client = LocationServices.getFusedLocationProviderClient(context);
        super.onAttach(context);
    }

    protected void getDeviceLocation() {
        try {
            Task<Location> locationResult = client.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), listener);
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
            client.removeLocationUpdates(this);
        }
    };

    protected void lookForLocation() {
        try {
            client.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected abstract void onLocationFound(Location location);
}

