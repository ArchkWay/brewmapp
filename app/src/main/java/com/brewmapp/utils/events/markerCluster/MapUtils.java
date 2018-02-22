package com.brewmapp.utils.events.markerCluster;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.brewmapp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nixus on 02.12.2017.
 */

public class MapUtils {

    public static String getCityName(Location location, Context context) {
        Geocoder geocoder = new Geocoder(context, new Locale("RU","ru"));
        //Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses != null ? addresses.get(0).getLocality() : null;
    }

    public static String calculateDistance(Location restoLocation, Location myLocation) {
        float[] distances = new float[1];
        Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(),
                restoLocation.getLatitude(), restoLocation.getLongitude(),
                distances);
        return String.valueOf(distances[0]);
    }

    public static Location getDefaultLocation(Context context) {
        Location location=new Location("gps");
        location.setLatitude(Float.valueOf(context.getResources().getString(R.string.default_Latitude)));
        location.setLongitude(Float.valueOf(context.getResources().getString(R.string.default_Longitude)));
        return location;
    }
}
