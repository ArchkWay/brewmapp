package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerPowerInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerPowerTypes extends ListResponse<BeerPowerInfo> {
    public BeerPowerTypes(@NonNull List<BeerPowerInfo> models) {
        super(models);
    }
}