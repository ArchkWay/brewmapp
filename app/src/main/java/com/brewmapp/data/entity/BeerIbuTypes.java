package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerIbuInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerIbuTypes extends ListResponse<BeerIbuInfo> {
    public BeerIbuTypes(@NonNull List<BeerIbuInfo> models) {
        super(models);
    }
}
