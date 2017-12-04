
package com.brewmapp.data.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class FilterRestoOnMap {

    @SerializedName("location_id")
    private String mLocationId;
    @SerializedName("location_lat")
    private String mLocationLat;
    @SerializedName("location_lon")
    private String mLocationLon;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private Object mNameEn;
    @SerializedName("resto_id")
    private String mRestoId;

    public String getLocationId() {
        return mLocationId;
    }

    public void setLocationId(String locationId) {
        mLocationId = locationId;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Object getNameEn() {
        return mNameEn;
    }

    public void setNameEn(Object nameEn) {
        mNameEn = nameEn;
    }

    public String getRestoId() {
        return mRestoId;
    }

    public void setRestoId(String restoId) {
        mRestoId = restoId;
    }

}
