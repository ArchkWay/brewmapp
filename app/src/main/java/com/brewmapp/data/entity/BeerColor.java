
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerColor {

    @SerializedName("getThumb")
    private String mGetThumb;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("name")
    private String mName;
    private boolean selected;

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

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
