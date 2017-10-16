package com.brewmapp.data.entity;

import java.util.List;

/**
 * Created by xpusher on 10/16/2017.
 */

public class Repost {
    private String id;
    private String related_model;
    private String related_id;
    private String name;
    private String user_getThumb;
    private String short_text;
    private String text;
    private User user_info;

    //private Resto user_resto_admin;
    private List<Photo> photo;

    public List<Photo> getPhoto() {
        return photo;
    }

    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }

//    public Resto getUser_resto_admin() {
//        return user_resto_admin;
//    }
//
//    public void setUser_resto_admin(Resto user_resto_admin) {
//        this.user_resto_admin = user_resto_admin;
//    }

    public User getUser_info() {
        return user_info;
    }

    public void setUser_info(User user_info) {
        this.user_info = user_info;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public String getShort_text() {
        return short_text;
    }

    public void setShort_text(String short_text) {
        this.short_text = short_text;
    }



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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_getThumb() {
        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }
}
