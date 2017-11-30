
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerPower {

    @SerializedName("getThumb")
    private String mGetThumb;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private Object mImage;
    @SerializedName("name")
    private String mName;
    @SerializedName("timestamp")
    private Object mTimestamp;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getGetThumb() {
        if(mGetThumb != null && !mGetThumb.startsWith("http")) {
            mGetThumb = BuildConfig.SERVER_ROOT_URL + mGetThumb;
        }
        return mGetThumb;
    }

    public void setGetThumb(String getThumb) {
        this.mGetThumb = getThumb;
    }
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Object getImage() {
        return mImage;
    }

    public void setImage(Object image) {
        mImage = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Object getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(Object timestamp) {
        mTimestamp = timestamp;
    }

}
