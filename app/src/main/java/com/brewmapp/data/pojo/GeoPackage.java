package com.brewmapp.data.pojo;

/**
 * Created by nixus on 02.12.2017.
 */

public class GeoPackage {

    private String countryId;
    private String regionId;
    private String coordStart;
    private String coordEnd;

    public GeoPackage() {
    }

    private String cityName;

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

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCoordStart(String coordStart) {
        this.coordStart = coordStart;
    }

    public String getCoordStart() {
        return coordStart;
    }

    public void setCoordEnd(String coordEnd) {
        this.coordEnd = coordEnd;
    }

    public String getCoordEnd() {
        return coordEnd;
    }
}
