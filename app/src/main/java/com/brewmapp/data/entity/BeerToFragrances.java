package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by xpusher on 10/30/2017.
 */

public class BeerToFragrances implements Serializable {

    private String beer_id;
    private String fragrance_id;

    public String getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(String beer_id) {
        this.beer_id = beer_id;
    }

    public String getFragrance_id() {
        return fragrance_id;
    }

    public void setFragrance_id(String fragrance_id) {
        this.fragrance_id = fragrance_id;
    }


}
