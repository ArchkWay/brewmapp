
package com.brewmapp.data.entity;

import android.widget.Filter;
import android.widget.Filterable;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

import eu.davidea.flexibleadapter.items.IFilterable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class RestoType implements Serializable, IFilterable {

    @SerializedName("getThumb")
    private String getThumb;
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

    public List<Relations> getRelations() {
        return Relations;
    }

    public void setRelations(List<Relations> relations) {
        Relations = relations;
    }

    @Override
    public boolean filter(String constraint) {
        return getName().contains(constraint);
    }
}
