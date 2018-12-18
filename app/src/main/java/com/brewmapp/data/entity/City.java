
package com.brewmapp.data.entity;

import javax.annotation.Generated;

import com.brewmapp.data.LocalizedStringsDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class City implements Serializable {

    @SerializedName("country_id")
    private String mCountryId;
    @SerializedName("id")
    private int mId;

    @JsonAdapter (LocalizedStringsDeserializer.class)
    @SerializedName("name")
    private LocalizedStrings mName;

    @SerializedName("phone_code")
    private String mPhoneCode;
    @SerializedName("region_id")
    private String mRegionId;
    @SerializedName("relations")
    private Relations mRelations;

    private boolean selected;

    private boolean selectable = false;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

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

    public void setName(String val) {
        //name = val;
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

    public boolean isSelectable() {

        return selectable;
    }
    public void setSelectable(boolean selectable) {

        this.selectable = selectable;
    }
}
