package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by xpusher on 10/30/2017.
 */

public class BeerType implements Serializable {

    private String id;
    private String image;
    private String name;
    private String name_en;
    private String name_wheel;
    private String parent_id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getName_wheel() {
        return name_wheel;
    }

    public void setName_wheel(String name_wheel) {
        this.name_wheel = name_wheel;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

}
