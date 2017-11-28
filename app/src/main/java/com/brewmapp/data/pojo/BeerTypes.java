
package com.brewmapp.data.pojo;

import javax.annotation.Generated;

import com.brewmapp.data.entity.*;
import com.brewmapp.data.entity.BeerType;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.List;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class BeerTypes {

    @SerializedName("getThumb")
    private String mGetThumb;
    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private String mNameEn;
    @SerializedName("name_wheel")
    private String mNameWheel;
    @SerializedName("parent_id")
    private String mParentId;

    public Object getmRelations() {
        return mRelations;
    }

    public void setmRelations(Object mRelations) {
        this.mRelations = mRelations;
    }

    @SerializedName("relations")
    private Object mRelations;
    private com.brewmapp.data.entity.BeerType beerType;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected;

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

    public Object getRelations() {
        return mRelations;
    }

    public BeerType getBeerType() {
        return beerType;
    }

    public void setBeerType(BeerType beerType) {
        this.beerType = beerType;
    }

    public static class DataStateDeserializer implements JsonDeserializer<BeerTypes> {

        @Override
        public BeerTypes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            BeerTypes beerTypes = new Gson().fromJson(json, BeerTypes.class);
            JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("relations")) {
                JsonElement elem = jsonObject.get("relations");
                if (elem != null && !elem.isJsonNull()) {
                    if (elem.isJsonArray()) {
                    } else {
//                        beerTypes.setmRelations(new BeerType());
                    }
                }
            }
            return beerTypes;
        }

    }
}
