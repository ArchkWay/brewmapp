
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Country {

    @SerializedName("country_code")
    private String mCountryCode;
    @SerializedName("getThumb")
    private String mGetThumb;
    @SerializedName("getThumbOriginal")
    private String mGetThumbOriginal;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private String mNameEn;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getGetThumb() {
        if(getGetThumb() != null && !getGetThumb().startsWith("http")) {
            mGetThumb = BuildConfig.SERVER_ROOT_URL + getGetThumb();
        }
        return getGetThumb();
    }

    public void setGetThumb(String getThumb) {
        this.mGetThumb = getThumb;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryCode(String countryCode) {
        mCountryCode = countryCode;
    }

    public String getGetThumbOriginal() {
        return mGetThumbOriginal;
    }

    public void setGetThumbOriginal(String getThumbOriginal) {
        mGetThumbOriginal = getThumbOriginal;
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

    public String getNameEn() {
        return mNameEn;
    }

    public void setNameEn(String nameEn) {
        mNameEn = nameEn;
    }

}
