package com.brewmapp.data.entity;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ovcst on 24.08.2017.
 */

public class Resto {
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
        return thumb;
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
}
