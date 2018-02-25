package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by Kras on 25.10.2017.
 */

public class Location implements Serializable {
    private String city_id;
    private LocationChild location;
    private Metro metro;

    public Location() {

    }


    public Location(android.location.Location location) {
        this.location=new LocationChild();
        this.location.setLat(location.getLatitude());
        this.location.setLon(location.getLongitude());
    }

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

    public String getFormatLocation(){
        String city="";
        String street="";
        String house="";

        city=city_id;
        try {street=getLocation().getStreet();}catch (Exception e){};
        try {house=getLocation().getHouse();}catch (Exception e){};
        return new StringBuilder()
                .append(city)
                .append(", ")
                .append(street)
                .append(", д.")
                .append(house)
                .toString();
    }

    public Location clone(){
        Location location=new Location();
        location.setCity_id(getCity_id());
        location.setLocation(getLocation().clone());
        return location;
    }
}
