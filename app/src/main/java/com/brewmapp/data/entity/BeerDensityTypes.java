package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerDensityInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerDensityTypes extends ListResponse<BeerDensityInfo> {
    public BeerDensityTypes(@NonNull List<BeerDensityInfo> models) {
        super(models);
    }
}