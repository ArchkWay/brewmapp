package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kras on 22.11.2017.
 */

public class Related_model_data {

    private String id;
    private String name;
    private String text;
    private String short_text;
    private String disposition_id;
    private String image;
    private String music;
    private String lunch_price;
    private String avg_cost;
    private String avg_cost_max;
    private String yes_count;
    private String user_id;
    private String timestamp;
    private String additional_data;
    private String updated_user_id;
    private String updated_at;
    private String location_id;
    private String getThumb;
    private String user_getThumb;
    private String like;
    private String dis_like;
    private String interested;
    private String no_interested;
    private String owner;

    private List<Photo> photo=new ArrayList<>();
    private Location locations;
    private User_info user_info;


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

    public String getDisposition_id() {
        return disposition_id;
    }

    public void setDisposition_id(String disposition_id) {
        this.disposition_id = disposition_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getLunch_price() {
        return lunch_price;
    }

    public void setLunch_price(String lunch_price) {
        this.lunch_price = lunch_price;
    }

    public String getAvg_cost() {
        return avg_cost;
    }

    public void setAvg_cost(String avg_cost) {
        this.avg_cost = avg_cost;
    }

    public String getAvg_cost_max() {
        return avg_cost_max;
    }

    public void setAvg_cost_max(String avg_cost_max) {
        this.avg_cost_max = avg_cost_max;
    }

    public String getYes_count() {
        return yes_count;
    }

    public void setYes_count(String yes_count) {
        this.yes_count = yes_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAdditional_data() {
        return additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    public String getUpdated_user_id() {
        return updated_user_id;
    }

    public void setUpdated_user_id(String updated_user_id) {
        this.updated_user_id = updated_user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getGetThumb() {
        if(getThumb!= null && !getThumb.startsWith("http")&& !getThumb.startsWith("/"))
            getThumb= BuildConfig.SERVER_ROOT_URL + getThumb;

        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public String getUser_getThumb() {
        if(user_getThumb!= null && !user_getThumb.startsWith("http")&& !user_getThumb.startsWith("/"))
            user_getThumb= BuildConfig.SERVER_ROOT_URL + user_getThumb;

        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getLocations() {
        return locations;
    }

    public void setLocations(Location locations) {
        this.locations = locations;
    }

    public User_info getUser_info() {
        return user_info;
    }

    public void setUser_info(User_info user_info) {
        this.user_info = user_info;
    }

    public List<Photo> getPhoto() {
        return photo;
    }







    public void setPhoto(List<Photo> photo) {
        this.photo = photo;
    }
}
