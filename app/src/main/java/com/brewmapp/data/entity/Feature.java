package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;

import java.io.Serializable;

/**
 * Created by Kras on 28.10.2017.
 */

public class Feature implements Serializable {
    private String id;
    private String name;
    private String image;
    private String getThumb;
    private String like;
    private String dis_like;
    private String interested;
    private String no_interested;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGetThumb() {
        if(getThumb!= null && !getThumb.startsWith("http")) {
            getThumb= BuildConfig.SERVER_ROOT_URL + getThumb;
        }
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDis_like() {
        return dis_like;
    }

    public void setDis_like(String dis_like) {
        this.dis_like = dis_like;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getNo_interested() {
        return no_interested;
    }

    public void setNo_interested(String no_interested) {
        this.no_interested = no_interested;
    }

}
