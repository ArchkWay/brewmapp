package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerColorInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerColorTypes extends ListResponse<BeerColorInfo> {
    public BeerColorTypes(@NonNull List<BeerColorInfo> models) {
        super(models);
    }
}