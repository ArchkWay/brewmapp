package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 21.10.2017.
 */

public class Beers extends ListResponse<BeerInfo> {
    public Beers(@NonNull List<BeerInfo> models) {
        super(models);
    }
}
