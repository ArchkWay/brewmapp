package com.brewmapp.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.brewmapp.data.entity.contract.Postable;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;

import ru.frosteye.ovsa.tool.DateTools;

/**
 * Created by oleg on 17.08.17.
 */

public class Post implements Postable, Serializable {

    private int id;
    private String text, name;
    private double lat, lon;
    private int like;
    private Date delayedDate;
    private boolean friendsOnly;
    private List<Integer> photoIds = new ArrayList<>();

    @SerializedName(Keys.DATE_NEWS)
    private Date date;

    @SerializedName(Keys.USER_INFO)
    private User user;

    public Date getDelayedDate() {
        return delayedDate;
    }

    public boolean isFriendsOnly() {
        return friendsOnly;
    }

    public void setFriendsOnly(boolean friendsOnly) {
        this.friendsOnly = friendsOnly;
    }

    public void setDelayedDate(Date delayedDate) {
        this.delayedDate = delayedDate;
    }

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
        if(friendsOnly) {
            params.addParam(Keys.ONLY_FRIENDS, 1);
        }
        if(delayedDate != null) {
            params.addParam(Keys.DATE_NEWS_DATE, DateTools.formatDashedDate(delayedDate));
            params.addParam(Keys.DATE_NEWS_TIMES, DateTools.formatTime(delayedDate));
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
