package com.brewmapp.data.entity;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable {

    private int id;
    private String name;
    private String nameEn;
    private int regionId;
    private int countryId;
    private int phoneCode;
    private List<BeerLocation.MetroInfo> metroList;
    private Country country;
    private Region region;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public int getRegionId() {
        return regionId;
    }

    public int getCountryId() {
        return countryId;
    }

    public int getPhoneCode() {
        return phoneCode;
    }

    public List<BeerLocation.MetroInfo> getMetroList() {
        return metroList;
    }

    public Country getCountry() {
        return country;
    }

    public Region getRegion() {
        return region;
    }
}
