package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BreweryInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 16.12.2017.
 */

public class Breweries extends ListResponse<BreweryInfo> {
    public Breweries(@NonNull List<BreweryInfo> models) {
        super(models);
    }
}
