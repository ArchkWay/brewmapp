
package com.brewmapp.data.entity;

import java.util.List;
import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerPack {

    @SerializedName("getThumb")
    private String getThumb;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("name")
    private String mName;
    @SerializedName("relations")
    private List<Relations> mRelations;
    private boolean selected;

    public BeerPack(String mName) {
        this.mName = mName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public List<Relations> getRelations() {
        return mRelations;
    }

    public void setRelations(List<Relations> relations) {
        mRelations = relations;
    }
}
