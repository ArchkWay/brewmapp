package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.data.pojo.SimpleImageSource;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ovcst on 24.08.2017.
 */

public class Resto implements ICommonItem, Serializable {
    private int id;
    private String name, text;
    private String site;
    private String music;

    @SerializedName(Keys.GET_THUMB)
    private String thumb;

    @SerializedName(Keys.AVG_COST)
    private int avgCost;

    @SerializedName(Keys.AVG_COST_MAX)
    private int avgCostMax;


    @SerializedName(Keys.PHOTO_COUNT)
    private int photoCount;

    @SerializedName(Keys.LOCATION_ID)
    private int locationId;

    private ImageSource imageSource;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getSite() {
        return site;
    }

    public String getMusic() {
        return music;
    }

    public String getThumb() {
        if(thumb != null && !thumb.startsWith("http")) {
            thumb = BuildConfig.SERVER_ROOT_URL + thumb;
        }
        return thumb;
    }

    public int getLocationId() {
        return locationId;
    }

    public int getAvgCost() {
        return avgCost;
    }

    public int getAvgCostMax() {
        return avgCostMax;
    }

    public int getPhotoCount() {
        return photoCount;
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
    public ImageSource image() {
        if(imageSource == null && getThumb() != null) {
            imageSource = new SimpleImageSource(getThumb());
        }
        return imageSource;
    }
}
