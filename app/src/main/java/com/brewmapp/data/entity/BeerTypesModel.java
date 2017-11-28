package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 27.11.2017.
 */

public class BeerTypesModel extends ListResponse<BeerTypeInfo> {
    public BeerTypesModel(@NonNull List<BeerTypeInfo> models) {
        super(models);
    }
}