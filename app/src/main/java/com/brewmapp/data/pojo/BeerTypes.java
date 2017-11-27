
package com.brewmapp.data.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerTypes {

    @SerializedName("getThumb")
    private String mGetThumb;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private Object mImage;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private String mNameEn;
    @SerializedName("name_wheel")
    private String mNameWheel;
    @SerializedName("parent_id")
    private String mParentId;
    @SerializedName("relations")
    private Relations mRelations;

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

    public String getNameWheel() {
        return mNameWheel;
    }

    public void setNameWheel(String nameWheel) {
        mNameWheel = nameWheel;
    }

    public String getParentId() {
        return mParentId;
    }

    public void setParentId(String parentId) {
        mParentId = parentId;
    }

    public Relations getRelations() {
        return mRelations;
    }

    public void setRelations(Relations relations) {
        mRelations = relations;
    }

}
