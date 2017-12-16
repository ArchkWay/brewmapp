
package com.brewmapp.data.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class City implements Serializable {

    @SerializedName("country_id")
    private String mCountryId;
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("name_en")
    private String mNameEn;
    @SerializedName("phone_code")
    private String mPhoneCode;
    @SerializedName("region_id")
    private String mRegionId;
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

    public String getNameEn() {
        return mNameEn;
    }

    public void setNameEn(String nameEn) {
        mNameEn = nameEn;
    }

    public String getPhoneCode() {
        return mPhoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        mPhoneCode = phoneCode;
    }

    public String getRegionId() {
        return mRegionId;
    }

    public void setRegionId(String regionId) {
        mRegionId = regionId;
    }

    public Relations getRelations() {
        return mRelations;
    }

    public void setRelations(Relations relations) {
        mRelations = relations;
    }

}
