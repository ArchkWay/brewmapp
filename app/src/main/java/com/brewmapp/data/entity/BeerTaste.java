
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.BuildConfig;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerTaste {

    @SerializedName("getThumb")
    private String GetThumb;
    @SerializedName("id")
    private String Id;
    @SerializedName("image")
    private String Image;
    @SerializedName("name")
    private String Name;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getGetThumb() {
        if(GetThumb != null && !GetThumb.startsWith("http")) {
            GetThumb = BuildConfig.SERVER_ROOT_URL + GetThumb;
        }
        return GetThumb;
    }

    public void setGetThumb(String getThumb) {
        this.GetThumb = getThumb;
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
}
