package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.data.pojo.SimpleImageSource;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestoLocation implements ICommonItem, Serializable {

    private int id;
    private String name;
    private ImageSource imageSource;
    private double location_lon, location_lat;
    private int metro_id;
    private String metro_name;

    @SerializedName(Keys.GET_THUMB)
    private String thumb;

    @SerializedName(Keys.LOCATION_ID)
    private int locationId;

    public RestoLocation(int id, String name, double location_lon, double location_lat) {
        this.id = id;
        this.name = name;
        this.location_lon = location_lon;
        this.location_lat = location_lat;
    }

    public String getThumb() {
        if(thumb != null && !thumb.startsWith("http")) {
            thumb = BuildConfig.SERVER_ROOT_URL + thumb;
        }
        return thumb;
    }

    @Override
    public String title() {
        return getName();
    }

    @Override
    public int id() {
        return getId();
    }

    @Override
    public ICommonItem.ImageSource image() {
        if(imageSource == null && getThumb() != null) {
            imageSource = new SimpleImageSource(getThumb());
        }
        return imageSource;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public double getLocation_lat() {
        return location_lat;
    }

    public double getLocation_lon() {
        return location_lon;
    }

    public int getMetro_id() {
        return metro_id;
    }

    public String getMetro_name() {
        return metro_name;
    }
}
