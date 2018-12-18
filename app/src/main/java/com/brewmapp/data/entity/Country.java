
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.brewmapp.data.LocalizedStringsDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Country implements Serializable {

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

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName("name")
    private LocalizedStrings mName;

    private boolean selected;
    private boolean selectable;

    public Country(String mName) {
//        this.mName = mName;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

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
        return mName != null ? mName.toString() : null;
    }

    public void setName(String name) {
        if(mName == null){
            mName = new LocalizedStrings();
        }

        mName.setString(name);
    }
}
