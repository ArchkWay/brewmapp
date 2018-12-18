package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kras on 04.11.2017.
 */

public class Review {
    private String id;
    private String user_id;
    private String related_model;
    private String related_id;
    private String text;
    private String type;
    private String timestamp;
    private String country_id;
    private String city_id12;
    private String getThumb;
    private User_info user_info ;

    @SerializedName ("user_getThumb")
    private UserGetThumb mUser_getThumb;

    private String like;
    private String dis_like;
    private String interested;
    private String no_interested;
    private Related_model_data related_model_data;


    public UserGetThumb getUser_getThumb() {
        return mUser_getThumb;
    }

    public void setUser_getThumb(UserGetThumb user_getThumb) {
        this.mUser_getThumb = user_getThumb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGetThumb() {
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public User_info getUser_info() {
        return user_info;
    }

    public void setUser_info(User_info user_info) {
        this.user_info = user_info;
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

    public Related_model_data getRelated_model_data() {

        return related_model_data;
    }
}
