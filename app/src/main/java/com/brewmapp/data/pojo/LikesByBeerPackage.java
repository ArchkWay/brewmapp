package com.brewmapp.data.pojo;

/**
 * Created by Kras on 16.11.2017.
 */

public class LikesByBeerPackage extends BasePackage{
    private String related_model;
    private String related_id;

    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }
}
