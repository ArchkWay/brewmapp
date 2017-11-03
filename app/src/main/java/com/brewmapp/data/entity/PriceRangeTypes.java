package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 03.11.2017.
 */

public class PriceRangeTypes extends ListResponse<PriceRangeInfo> {
    public PriceRangeTypes(@NonNull List<PriceRangeInfo> models) {
        super(models);
    }
}