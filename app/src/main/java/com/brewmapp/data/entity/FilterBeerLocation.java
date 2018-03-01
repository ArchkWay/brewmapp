package com.brewmapp.data.entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by nixus on 27.11.2017.
 */

public class FilterBeerLocation implements ClusterItem {

    @SerializedName("beer_id")
    private String mBeerId;
    @SerializedName("location_lat")
    private String mLocationLat;
    @SerializedName("location_lon")
    private String mLocationLon;
    @SerializedName("resto_id")
    private String mRestoId;

    public String getmBeerId() {
        return mBeerId;
    }

    public void setmBeerId(String beerId) {
        mBeerId = beerId;
    }

    public String getLocationLat() {
        return mLocationLat;
    }

    public void setLocationLat(String locationLat) {
        mLocationLat = locationLat;
    }

    public String getLocationLon() {
        return mLocationLon;
    }

    public void setLocationLon(String locationLon) {
        mLocationLon = locationLon;
    }

    public String getRestoId() {
        return mRestoId;
    }

    public void setRestoId(String restoId) {
        mRestoId = restoId;
    }

    @Override
    public LatLng getPosition() {
        return null;
    }

    public String getTitle() {
        return null;
    }

    public String getSnippet() {
        return null;
    }
}

