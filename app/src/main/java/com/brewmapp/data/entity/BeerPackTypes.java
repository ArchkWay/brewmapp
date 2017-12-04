package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerPackInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerPackTypes extends ListResponse<BeerPackInfo> {
    public BeerPackTypes(@NonNull List<BeerPackInfo> models) {
        super(models);
    }
}