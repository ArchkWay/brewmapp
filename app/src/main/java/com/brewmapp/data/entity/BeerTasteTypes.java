package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerTasteInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerTasteTypes extends ListResponse<BeerTasteInfo> {
    public BeerTasteTypes(@NonNull List<BeerTasteInfo> models) {
        super(models);
    }
}