package ru.frosteye.beermap.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.frosteye.beermap.data.entity.contract.Postable;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;

/**
 * Created by oleg on 17.08.17.
 */

public class Post implements Postable {

    private int id;
    private String text, name;
    private double lat, lon;
    private int like;
    private List<Integer> photoIds = new ArrayList<>();

    @SerializedName(Keys.TIMESTAMP)
    private Date date;

    @SerializedName(Keys.USER_INFO)
    private User user;

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getLikes() {
        return like;
    }

    public List<Integer> getPhotoIds() {
        return photoIds;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
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

    public boolean validate() {
        return text != null && !text.isEmpty()
                && name != null && !name.isEmpty();
    }

    public boolean isStarted() {
        return text != null && !text.isEmpty()
                || name != null && !name.isEmpty()
                || photoIds.size() > 0;
    }

    @Override
    public WrapperParams createParams() {
        WrapperParams params = new WrapperParams(Wrappers.NEWS);
        params.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
        params.addParam(Keys.TEXT, text);
        params.addParam(Keys.NAME, name);
        if(lat != 0) {
            params.addParam(Keys.LAT, lat);
            params.addParam(Keys.LAT, lon);
        }
        if(!getPhotoIds().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for(Integer integer: getPhotoIds()) {
                builder.append(",").append(integer);
            }
            params.addParam(Keys.PHOTOS_ID, builder.toString().substring(1));
        }
        return params;
    }
}
