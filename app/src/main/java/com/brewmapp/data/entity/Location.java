package com.brewmapp.data.entity;

/**
 * Created by Kras on 25.10.2017.
 */

public class Location {
    private String city_id;
    private LocationChild location;
    private Metro metro;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public LocationChild getLocation() {
        return location;
    }

    public void setLocation(LocationChild location) {
        this.location = location;
    }

    public Metro getMetro() {
        return metro;
    }

    public void setMetro(Metro metro) {
        this.metro = metro;
    }
}
