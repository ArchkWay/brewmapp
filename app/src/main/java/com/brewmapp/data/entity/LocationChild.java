package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by Kras on 25.10.2017.
 */

public class LocationChild implements Serializable{

    private String additional;
    private String by_user_id;
    private String city_id;
    private String country_id;
    private String house;
    private String id;
    private double lat;
    private double lon;
    private String metro_id;
    private String name;
    private String region_id;
    private String street;
    private String timestamp;


    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getBy_user_id() {
        return by_user_id;
    }

    public void setBy_user_id(String by_user_id) {
        this.by_user_id = by_user_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getMetro_id() {
        return metro_id;
    }

    public void setMetro_id(String metro_id) {
        this.metro_id = metro_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public LocationChild clone(){
        LocationChild locationChild=new LocationChild();
        locationChild.setStreet(getStreet());
        locationChild.setHouse(getHouse());
        return locationChild;
    }


}
