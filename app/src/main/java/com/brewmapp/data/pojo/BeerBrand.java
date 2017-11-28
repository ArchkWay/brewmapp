
package com.brewmapp.data.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerBrand {

    @SerializedName("brewery_id")
    private Object mBreweryId;
    @SerializedName("country_id")
    private Object mCountryId;
    @SerializedName("dis_like")
    private String mDisLike;
    @SerializedName("getThumb")
    private String mGetThumb;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("interested")
    private String mInterested;
    @SerializedName("like")
    private String mLike;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_ru")
    private Object mNameRu;
    @SerializedName("no_interested")
    private String mNoInterested;
    @SerializedName("relations")
    private Relations mRelations;
    @SerializedName("timestamp")
    private Object mTimestamp;

    public Object getBreweryId() {
        return mBreweryId;
    }

    public void setBreweryId(Object breweryId) {
        mBreweryId = breweryId;
    }

    public Object getCountryId() {
        return mCountryId;
    }

    public void setCountryId(Object countryId) {
        mCountryId = countryId;
    }

    public String getDisLike() {
        return mDisLike;
    }

    public void setDisLike(String disLike) {
        mDisLike = disLike;
    }

    public String getGetThumb() {
        return mGetThumb;
    }

    public void setGetThumb(String getThumb) {
        mGetThumb = getThumb;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getInterested() {
        return mInterested;
    }

    public void setInterested(String interested) {
        mInterested = interested;
    }

    public String getLike() {
        return mLike;
    }

    public void setLike(String like) {
        mLike = like;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Object getNameRu() {
        return mNameRu;
    }

    public void setNameRu(Object nameRu) {
        mNameRu = nameRu;
    }

    public String getNoInterested() {
        return mNoInterested;
    }

    public void setNoInterested(String noInterested) {
        mNoInterested = noInterested;
    }

    public Relations getRelations() {
        return mRelations;
    }

    public void setRelations(Relations relations) {
        mRelations = relations;
    }

    public Object getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Object timestamp) {
        mTimestamp = timestamp;
    }

}
