package com.brewmapp.data.pojo;

/**
 * Created by xpusher on 10/31/2017.
 */

public class SubscriptionPackage extends BasePackage{
    private String related_model;
    private String related_id;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
