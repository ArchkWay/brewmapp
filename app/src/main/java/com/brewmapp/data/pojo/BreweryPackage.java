package com.brewmapp.data.pojo;

/**
 * Created by nixus on 08.12.2017.
 */

public class BreweryPackage {

    private String id;
    private int page;
    private String countryId;
    private String locationId;
    private String beerCountryId;
    private String beerBrandId;
    private String beerTypeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getBeerCountryId() {
        return beerCountryId;
    }

    public void setBeerCountryId(String beerCountryId) {
        this.beerCountryId = beerCountryId;
    }

    public String getBeerBrandId() {
        return beerBrandId;
    }

    public void setBeerBrandId(String beerBrandId) {
        this.beerBrandId = beerBrandId;
    }

    public String getBeerTypeId() {
        return beerTypeId;
    }

    public void setBeerTypeId(String beerTypeId) {
        this.beerTypeId = beerTypeId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
