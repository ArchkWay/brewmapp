
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.data.LocalizedStringsDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Metro implements Serializable {

    @SerializedName("city_id")
    private String mCityId;
    @SerializedName("id")
    private String mId;
    @SerializedName("lat")
    private String mLat;
    @SerializedName("lon")
    private String mLon;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName("name")
    private LocalizedStrings mName;

    @SerializedName("distance")
    private int distance;


    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getCityId() {
        return mCityId;
    }

    public void setCityId(String cityId) {
        mCityId = cityId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLon() {
        return mLon;
    }

    public void setLon(String lon) {
        mLon = lon;
    }

    public String getName() {
        return mName != null ? mName.toString() : null;
    }

    public void setName(String name) {
//        mName = name;
    }

}
