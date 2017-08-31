package com.brewmapp.data.entity;

import com.brewmapp.data.entity.contract.Postable;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.frosteye.ovsa.tool.DateTools;

/**
 * Created by oleg on 17.08.17.
 */

public class Sale implements Serializable, ILikeable {

    private int id;
    private String text, name;
    private double lat, lon;
    private int like;

    @SerializedName(Keys.DATE_START)
    private Date dateStart;

    @SerializedName(Keys.DATE_END)
    private Date dateEnd;

    @SerializedName(Keys.DIS_LIKE)
    private int dislike;

    @SerializedName(Keys.USER_INFO)
    private User user;

    @SerializedName(Keys.PARENT_INFO)
    private Resto parent;

    private List<Photo> photo;

    public int getId() {
        return id;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Resto getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getLike() {
        return like;
    }

    public int getDislike() {
        return dislike;
    }

    public User getUser() {
        return user;
    }

    public List<Photo> getPhotos() {
        return photo;
    }

    @Override
    public void increaseLikes() {
        like++;
    }
}
