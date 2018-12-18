
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.data.LocalizedStringsDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Region implements Serializable {

    @SerializedName("country_id")
    private String mCountryId;
    @SerializedName("id")
    private int mId;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName("name")
    private LocalizedStrings mName;

//    @SerializedName("name_en")
//    private Object mNameEn;

    @SerializedName("relations")
    private Relations mRelations;

    private boolean selectable = false;

    public String getCountryId() {
        return mCountryId;
    }

    public void setCountryId(String countryId) {
        mCountryId = countryId;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName != null ? mName.toString() : null;
    }

    public void setName(String name) {
//        mName = name;
    }

    public Object getNameEn() {
        return mName != null ? mName.getString("58") : null;
    }

    public void setNameEn(Object nameEn) {
//        mNameEn = nameEn;
    }

    public Relations getRelations() {
        return mRelations;
    }

    public void setRelations(Relations relations) {
        mRelations = relations;
    }


    public boolean isSelectable() {

        return selectable;
    }
    public void setSelectable(boolean selectable) {

        this.selectable = selectable;
    }
}
