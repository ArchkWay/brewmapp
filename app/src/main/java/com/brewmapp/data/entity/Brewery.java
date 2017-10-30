package com.brewmapp.data.entity;

import java.io.Serializable;

/**
 * Created by xpusher on 10/30/2017.
 */

public class Brewery implements Serializable {
    private String additional_data;
    private String avg_ball;
    private String by_user_id;
    private String id;
    private String image;
    private String in_archive;
    private String location_id;
    private String name;
    private String name_ru;
    private String short_text;
    private String text;
    private String timestamp;

    public String getAdditional_data() {
        return additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    public String getAvg_ball() {
        return avg_ball;
    }

    public void setAvg_ball(String avg_ball) {
        this.avg_ball = avg_ball;
    }

    public String getBy_user_id() {
        return by_user_id;
    }

    public void setBy_user_id(String by_user_id) {
        this.by_user_id = by_user_id;
    }

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

    public String getIn_archive() {
        return in_archive;
    }

    public void setIn_archive(String in_archive) {
        this.in_archive = in_archive;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }

    public String getShort_text() {
        return short_text;
    }

    public void setShort_text(String short_text) {
        this.short_text = short_text;
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
}
