
package com.brewmapp.data.pojo;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Relations {

    @SerializedName("country")
    private List<String> mCountry;

    public List<String> getCountry() {
        return mCountry;
    }

    public void setCountry(List<String> country) {
        mCountry = country;
    }

}
