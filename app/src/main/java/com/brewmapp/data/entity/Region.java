
package com.brewmapp.data.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Region {

    @SerializedName("country_id")
    private String mCountryId;
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private Object mNameEn;
    @SerializedName("relations")
    private Relations mRelations;

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
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Object getNameEn() {
        return mNameEn;
    }

    public void setNameEn(Object nameEn) {
        mNameEn = nameEn;
    }

    public Relations getRelations() {
        return mRelations;
    }

    public void setRelations(Relations relations) {
        mRelations = relations;
    }

}
