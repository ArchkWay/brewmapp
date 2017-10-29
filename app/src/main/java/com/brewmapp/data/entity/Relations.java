
package com.brewmapp.data.entity;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Relations {

    @SerializedName("Country")
    private com.brewmapp.data.entity.Country mCountry;
    @SerializedName("Metro")
    private List<com.brewmapp.data.entity.Metro> mMetro;
    @SerializedName("Region")
    private com.brewmapp.data.entity.Region mRegion;

    public com.brewmapp.data.entity.Country getCountry() {
        return mCountry;
    }

    public void setCountry(com.brewmapp.data.entity.Country Country) {
        mCountry = Country;
    }

    public List<com.brewmapp.data.entity.Metro> getMetro() {
        return mMetro;
    }

    public void setMetro(List<com.brewmapp.data.entity.Metro> Metro) {
        mMetro = Metro;
    }

    public com.brewmapp.data.entity.Region getRegion() {
        return mRegion;
    }

    public void setRegion(com.brewmapp.data.entity.Region Region) {
        mRegion = Region;
    }

}
