package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;

import java.io.Serializable;

/**
 * Created by xpusher on 11/27/2017.
 */

public class Resto_info implements Serializable {
    private String id;
    private String name;
    private String text;
    private String short_text;
    private String disposition_id;
    private String image;
    private String site;
    private String music;
    private String lunch_price;
    private String avg_cost;
    private String avg_cost_max;
    private String user_id;
    private String advertising;
    private String gpa;
    private String timestamp;
    private String in_archive;
    private String additional_data;
    private String type_id;
    private String round_clock;
    private String name_en;
    private String updated_user_id;
    private String updated_at;
    private String location_id;
    private String getThumb;

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

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIn_archive() {
        return in_archive;
    }

    public void setIn_archive(String in_archive) {
        this.in_archive = in_archive;
    }

    public String getAdditional_data() {
        return additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getRound_clock() {
        return round_clock;
    }

    public void setRound_clock(String round_clock) {
        this.round_clock = round_clock;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
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
        if(getThumb != null && !getThumb.startsWith("http")) {
            getThumb = BuildConfig.SERVER_ROOT_URL + getThumb;
        }

        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }
}
