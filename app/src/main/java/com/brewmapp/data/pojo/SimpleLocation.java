package com.brewmapp.data.pojo;

import com.brewmapp.data.model.ILocation;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by oleg on 25.09.17.
 */

public class SimpleLocation implements ILocation, Serializable {

    private LatLng position;
    private String title;

    public SimpleLocation(double lat, double lng, String title) {
        this.position = new LatLng(lat, lng);
        this.title = title;
    }

    @Override
    public LatLng position() {
        return position;
    }

    @Override
    public String title() {
        return title;
    }
}
