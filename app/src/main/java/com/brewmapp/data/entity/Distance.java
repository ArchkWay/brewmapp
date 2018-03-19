package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kras on 24.02.2018.
 */

public class Distance implements Serializable {
    private String distance;
    private String city_name;
    @SerializedName("disable_json_serialise")
    private List<Metro> metro;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDistance() {
        return distance;
    }
    public String getFormatedDistance() {
        return String.format(Locale.getDefault(),"%.1f км",Double.valueOf(distance)/1000);
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<Metro> getMetro() {
        return metro;
    }

    public void setMetro(List<Metro> metro) {
        this.metro = metro;
    }
}
