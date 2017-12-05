package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BreweryInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 05.12.2017.
 */

public class BreweryTypes extends ListResponse<BreweryInfo> {
    public BreweryTypes(@NonNull List<BreweryInfo> models) {
        super(models);
    }
}