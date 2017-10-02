package com.brewmapp.execution.exchange.response;

import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleg on 30.09.17.
 */

public class QuickSearchResponse {

    @SerializedName(Keys.CAP_RESTO)
    private List<Resto> restos;

    @SerializedName(Keys.CAP_LOCATION)
    private List<BeerLocation.LocationInfo> locations;

    public List<Resto> getRestos() {
        return restos;
    }

    public List<BeerLocation.LocationInfo> getLocations() {
        return locations;
    }
}
