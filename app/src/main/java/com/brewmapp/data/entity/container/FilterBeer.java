package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.FilterBeerInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 17.11.2017.
 */

public class FilterBeer extends ListResponse<FilterBeerInfo> {
    public FilterBeer(@NonNull List<FilterBeerInfo> models) {
        super(models);
    }
}
