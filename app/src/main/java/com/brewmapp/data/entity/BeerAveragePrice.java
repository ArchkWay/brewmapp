package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by xpusher on 10/30/2017.
 */

public class BeerAveragePrice implements Serializable {
    private String beer_id;
    private String created_at;
    private String id;
    private String price;
    private String resto_menu_capacity_id;
    private String resto_menu_packing_id;


    public String getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(String beer_id) {
        this.beer_id = beer_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getResto_menu_capacity_id() {
        return resto_menu_capacity_id;
    }

    public void setResto_menu_capacity_id(String resto_menu_capacity_id) {
        this.resto_menu_capacity_id = resto_menu_capacity_id;
    }

    public String getResto_menu_packing_id() {
        return resto_menu_packing_id;
    }

    public void setResto_menu_packing_id(String resto_menu_packing_id) {
        this.resto_menu_packing_id = resto_menu_packing_id;
    }

}
