package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by xpusher on 10/30/2017.
 */

public class BeerToColors implements Serializable {
    private String beer_id;
    private String color_id;

    public String getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(String beer_id) {
        this.beer_id = beer_id;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }
}
