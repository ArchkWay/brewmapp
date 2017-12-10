
package com.brewmapp.data.entity;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class AvgPrice {

    @SerializedName("beer_id")
    private String mBeerId;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("id")
    private String mId;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("resto_menu_capacity_id")
    private String mRestoMenuCapacityId;
    @SerializedName("resto_menu_packing_id")
    private String mRestoMenuPackingId;

    public String getBeerId() {
        return mBeerId;
    }

    public void setBeerId(String beerId) {
        mBeerId = beerId;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getRestoMenuCapacityId() {
        return mRestoMenuCapacityId;
    }

    public void setRestoMenuCapacityId(String restoMenuCapacityId) {
        mRestoMenuCapacityId = restoMenuCapacityId;
    }

    public String getRestoMenuPackingId() {
        return mRestoMenuPackingId;
    }

    public void setRestoMenuPackingId(String restoMenuPackingId) {
        mRestoMenuPackingId = restoMenuPackingId;
    }

}
