package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
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
    private String user_getThumb;
    private String related_model;
    private String related_id;

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

    public String getUser_getThumb() {
        if(user_getThumb != null && !user_getThumb.startsWith("http")) {
            user_getThumb= BuildConfig.SERVER_ROOT_URL + user_getThumb;
        }

        return user_getThumb;
    }

    public void setUser_getThumb(String user_getThumb) {
        this.user_getThumb = user_getThumb;
    }

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

    @Override
    public void increaseDisLikes() {
        dislike++;
    }

    public String getDateStartFormated() {
        try {
            return android.text.format.DateFormat.format("dd MMMM yyyy",getDateStart()).toString();
        }catch (Exception e){
            return null;
        }
    }
}
