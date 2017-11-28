package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerBrandTypes extends ListResponse<BeerBrandInfo> {
    public BeerBrandTypes(@NonNull List<BeerBrandInfo> models) {
        super(models);
    }
}