package com.brewmapp.data.entity;

import android.location.*;
import android.location.Location;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.data.pojo.SimpleImageSource;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class RestoLocation implements ICommonItem, Serializable {

    private int id;
    private String name;
    private ImageSource imageSource;
    private double location_lon, location_lat;
    private int metro_id;
    private String metro_name;
    private String resto_id;
    private HashMap<String,String> beersId=new HashMap<>();

    @SerializedName(Keys.GET_THUMB)
    private String thumb;

    @SerializedName(Keys.LOCATION_ID)
    private int locationId;

    public RestoLocation(int id, String name, double location_lat, double location_lon) {
        this.id = id;
        this.name = name;
        this.location_lat = location_lat;
        this.location_lon = location_lon;
    }

    public RestoLocation(Event event) {
        location_lon=event.getLocation().getInfo().getLon();
        location_lat=event.getLocation().getInfo().getLat();
    }

    public RestoLocation(String restoId, Integer id, String name, double location_lat, double location_lon) {
        this.resto_id=restoId;
        this.id = id;
        this.name = name;
        this.location_lat = location_lat;
        this.location_lon = location_lon;

    }

    public String getThumb() {
        if(thumb != null && !thumb.startsWith("http")) {
            thumb = BuildConfig.SERVER_ROOT_URL + thumb;
        }
        return thumb;
    }

    public String getResto_id() {
        return resto_id;
    }

    public void setResto_id(String resto_id) {
        this.resto_id = resto_id;
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

    public void setLocation_lon(double location_lon) {
        this.location_lon = location_lon;
    }

    public void setLocation_lat(double location_lat) {
        this.location_lat = location_lat;
    }

    public int getMetro_id() {
        return metro_id;
    }

    public String getMetro_name() {
        return metro_name;
    }

    public android.location.Location getLocation() {
        android.location.Location location = new Location("gps");
        location.setLatitude(getLocation_lat());
        location.setLongitude(getLocation_lon());
        return location;
    }

    public HashMap<String,String> getBeersId() {
        return beersId;
    }

    public void setBeersId(HashMap<String,String> beersIs) {
        this.beersId = beersIs;
    }
}
