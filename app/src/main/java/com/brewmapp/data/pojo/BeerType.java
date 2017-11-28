
package com.brewmapp.data.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerType {

    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private Object mImage;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private String mNameEn;
    @SerializedName("name_wheel")
    private Object mNameWheel;
    @SerializedName("parent_id")
    private Object mParentId;

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

    public String getNameEn() {
        return mNameEn;
    }

    public void setNameEn(String nameEn) {
        mNameEn = nameEn;
    }

    public Object getNameWheel() {
        return mNameWheel;
    }

    public void setNameWheel(Object nameWheel) {
        mNameWheel = nameWheel;
    }

    public Object getParentId() {
        return mParentId;
    }

    public void setParentId(Object parentId) {
        mParentId = parentId;
    }

}
