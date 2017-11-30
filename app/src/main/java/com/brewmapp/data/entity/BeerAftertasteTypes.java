package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BeerAftertasteInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerAftertasteTypes extends ListResponse<BeerAftertasteInfo> {
    public BeerAftertasteTypes(@NonNull List<BeerAftertasteInfo> models) {
        super(models);
    }
}