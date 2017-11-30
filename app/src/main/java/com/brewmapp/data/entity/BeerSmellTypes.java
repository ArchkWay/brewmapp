package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerSmellInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerSmellTypes extends ListResponse<BeerSmellInfo> {
    public BeerSmellTypes(@NonNull List<BeerSmellInfo> models) {
        super(models);
    }
}