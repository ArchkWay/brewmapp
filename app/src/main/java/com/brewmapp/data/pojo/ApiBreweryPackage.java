package com.brewmapp.data.pojo;

/**
 * Created by xpusher on 02.02.2018.
 */

public class ApiBreweryPackage {
private String id=null;
private String country_id=null;
private String location_id=null;
private String beer_country_id=null;
private String beer_brand_id=null;
private String beer_type_id=null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getBeer_country_id() {
        return beer_country_id;
    }

    public void setBeer_country_id(String beer_country_id) {
        this.beer_country_id = beer_country_id;
    }

    public String getBeer_brand_id() {
        return beer_brand_id;
    }

    public void setBeer_brand_id(String beer_brand_id) {
        this.beer_brand_id = beer_brand_id;
    }

    public String getBeer_type_id() {
        return beer_type_id;
    }

    public void setBeer_type_id(String beer_type_id) {
        this.beer_type_id = beer_type_id;
    }
}
