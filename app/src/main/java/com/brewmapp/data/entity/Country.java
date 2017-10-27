package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.data.pojo.SimpleImageSource;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nlbochas on 25/10/2017.
 */

public class Country implements ICommonItem, Serializable {

    private int id;
    private String name;
    private String nameEn;
    private ICommonItem.ImageSource imageSource;
    private String countryCode;

    @SerializedName(Keys.GET_THUMB)
    private String thumb;

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

}
