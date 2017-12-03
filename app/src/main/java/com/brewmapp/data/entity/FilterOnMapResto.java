package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nixus on 04.12.2017.
 */

public class FilterOnMapResto {

    @SerializedName("location_id")
    private String mLocationId;
    @SerializedName("location_lat")
    private double mLocationLat;
    @SerializedName("location_lon")
    private double mLocationLon;
    @SerializedName("resto_id")
    private String mRestoId;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private String mNameEn;
    @SerializedName("beer_id")
    private String mBeerId;

    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String locationId) {
        mLocationId = locationId;
    }

    public double getLocationLat() {
        return mLocationLat;
    }

    public void setLocationLat(double locationLat) {
        mLocationLat = locationLat;
    }

    public double getLocationLon() {
        return mLocationLon;
    }

    public void setLocationLon(double locationLon) {
        mLocationLon = locationLon;
    }

    public String getRestoId() {
        return mRestoId;
    }

    public void setRestoId(String restoId) {
        mRestoId = restoId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNameEn() {
        return mNameEn;
    }

    public void setmNameEn(String mNameEn) {
        this.mNameEn = mNameEn;
    }

}
