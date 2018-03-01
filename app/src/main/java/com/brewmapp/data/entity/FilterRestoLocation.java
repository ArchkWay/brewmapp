
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FilterRestoLocation implements ClusterItem {

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

    private LatLng mPosition;
    private String snippet;
    private String title;

    public FilterRestoLocation(String title, String snippet) {
        this.title = title;
        this.snippet = snippet;
    }

    public FilterRestoLocation(FilterRestoOnMap model) {
        setLocationId(model.getLocationId());
        setLocationLat(Double.valueOf(model.getLocationLat()));
        setLocationLon(Double.valueOf(model.getLocationLon()));
        setRestoId(model.getRestoId());
        setmName(model.getName());
        setmNameEn(getmNameEn());
    }

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


    @Override
    public LatLng getPosition() {
        return new LatLng(getLocationLat(), getLocationLon());
    }

    public String getTitle() {
        return mName;
    }

    public String getSnippet() {
        return snippet;
    }

    public BitmapDescriptor getIcon() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_green);
    }

    public String getmBeerId() {
        return mBeerId;
    }

    public void setmBeerId(String mBeerId) {
        this.mBeerId = mBeerId;
    }

}
