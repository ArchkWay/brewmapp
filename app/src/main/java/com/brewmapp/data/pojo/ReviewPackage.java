package com.brewmapp.data.pojo;

/**
 * Created by Kras on 04.11.2017.
 */

public class ReviewPackage extends BasePackage{
    private String related_model;
    private String related_id;
    private String text;
    private String user_id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRelated_id() {
        return related_id;
    }

    public void setRelated_id(String related_id) {
        this.related_id = related_id;
    }

    public String getRelated_model() {
        return related_model;
    }

    public void setRelated_model(String related_model) {
        this.related_model = related_model;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
