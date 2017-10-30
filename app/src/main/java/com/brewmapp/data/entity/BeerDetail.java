package com.brewmapp.data.entity;

/**
 * Created by Kras on 30.10.2017.
 */

public class BeerDetail {

    private Beer beer;

    public BeerDetail(Beer beer) {
        this.beer=beer;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }


}
