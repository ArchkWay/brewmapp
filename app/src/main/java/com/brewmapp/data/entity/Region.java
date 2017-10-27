package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by nlbochas on 25/10/2017.
 */

public class Region implements Serializable {

    private int id;
    private String name;
    private int countryId;
    private Country country;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCountryId() {
        return countryId;
    }

    public Country getCountry() {
        return country;
    }
}
