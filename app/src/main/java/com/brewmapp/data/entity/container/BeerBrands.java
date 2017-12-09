package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 10.12.2017.
 */

public class BeerBrands  extends ListResponse<BeerBrandInfo> {
    public BeerBrands(@NonNull List<BeerBrandInfo> models) {
        super(models);
    }
}
