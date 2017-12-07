
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerBrand implements Serializable {

    @SerializedName("brewery_id")
    private Object BreweryId;
    @SerializedName("country_id")
    private Object CountryId;
    @SerializedName("dis_like")
    private String DisLike;
    @SerializedName("getThumb")
    private String getThumb;
    @SerializedName("id")
    private String Id;
    @SerializedName("image")
    private String Image;
    @SerializedName("interested")
    private String Interested;
    @SerializedName("like")
    private String Like;
    @SerializedName("name")
    private String Name;
    @SerializedName("name_ru")
    private Object NameRu;
    @SerializedName("no_interested")
    private String NoInterested;
    @SerializedName("relations")
    private com.brewmapp.data.entity.Relations Relations;
    @SerializedName("timestamp")
    private Object Timestamp;

    private boolean selected;

    public BeerBrand(String name) {
        Name = name;
    }

    public Object getBreweryId() {
        return BreweryId;
    }

    public void setBreweryId(Object breweryId) {
        BreweryId = breweryId;
    }

    public Object getCountryId() {
        return CountryId;
    }

    public void setCountryId(Object countryId) {
        CountryId = countryId;
    }

    public String getDisLike() {
        return DisLike;
    }

    public void setDisLike(String disLike) {
        DisLike = disLike;
    }

    public String getGetThumb() {
        if(getThumb != null && !getThumb.startsWith("http")) {
            getThumb = BuildConfig.SERVER_ROOT_URL + getThumb;
        }
        return getThumb;
    }

    public void setGetThumb(String getThumb) {
        this.getThumb = getThumb;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getInterested() {
        return Interested;
    }

    public void setInterested(String interested) {
        Interested = interested;
    }

    public String getLike() {
        return Like;
    }

    public void setLike(String like) {
        Like = like;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Object getNameRu() {
        return NameRu;
    }

    public void setNameRu(Object nameRu) {
        NameRu = nameRu;
    }

    public String getNoInterested() {
        return NoInterested;
    }

    public void setNoInterested(String noInterested) {
        NoInterested = noInterested;
    }

    public com.brewmapp.data.entity.Relations getRelations() {
        return Relations;
    }

    public void setRelations(com.brewmapp.data.entity.Relations relations) {
        Relations = relations;
    }

    public Object getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(Object timestamp) {
        Timestamp = timestamp;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
