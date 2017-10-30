package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by xpusher on 10/30/2017.
 */

public class BeerToAftertastes implements Serializable {
    private String aftertaste_id;
    private String beer_id;

    public String getAftertaste_id() {
        return aftertaste_id;
    }

    public void setAftertaste_id(String aftertaste_id) {
        this.aftertaste_id = aftertaste_id;
    }

    public String getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(String beer_id) {
        this.beer_id = beer_id;
    }
}
