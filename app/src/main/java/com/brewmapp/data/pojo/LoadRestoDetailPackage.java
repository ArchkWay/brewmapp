package com.brewmapp.data.pojo;

/**
 * Created by Kras on 26.10.2017.
 */

public class LoadRestoDetailPackage extends BasePackage{
    private String id;
    private double lat;
    private double lon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }
}
