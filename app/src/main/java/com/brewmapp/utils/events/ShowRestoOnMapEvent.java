package com.brewmapp.utils.events;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.entity.RestoLocation;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class ShowRestoOnMapEvent {

    private List<FilterRestoLocation> restoLocationList;

    public ShowRestoOnMapEvent(List<FilterRestoLocation> restoLocationList) {
        this.restoLocationList = restoLocationList;
    }

    public List<FilterRestoLocation> getRestoLocationList() {
        return restoLocationList;
    }
}
