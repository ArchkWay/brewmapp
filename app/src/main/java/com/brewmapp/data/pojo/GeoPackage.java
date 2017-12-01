package com.brewmapp.data.pojo;

/**
 * Created by nixus on 02.12.2017.
 */

public class GeoPackage {

    private String countryId;
    private String regionId;

    public GeoPackage(String countryId, String regionId) {
        this.countryId = countryId;
        this.regionId = regionId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
