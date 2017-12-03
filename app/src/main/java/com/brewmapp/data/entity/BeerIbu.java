
package com.brewmapp.data.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerIbu {

    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private Object mImage;
    @SerializedName("name")
    private String mName;
    private boolean selected;

    public BeerIbu(String mName) {
        this.mName = mName;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
