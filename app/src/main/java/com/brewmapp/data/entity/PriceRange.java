
package com.brewmapp.data.entity;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

import eu.davidea.flexibleadapter.items.IFilterable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PriceRange implements Serializable {

    @SerializedName("getThumb")
    private String getThumb;
    @SerializedName("value")
    private String value;
    @SerializedName("id")
    private String Id;
    @SerializedName("image")
    private String Image;
    @SerializedName("name")
    private String Name;
    @SerializedName("relations")
    private List<Relations> Relations;

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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        value = value;
    }

    public List<Relations> getRelations() {
        return Relations;
    }

    public void setRelations(List<Relations> relations) {
        Relations = relations;
    }
}
