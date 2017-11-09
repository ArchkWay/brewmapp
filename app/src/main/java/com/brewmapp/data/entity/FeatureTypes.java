package com.brewmapp.data.entity;

import com.brewmapp.data.entity.wrapper.FeatureInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by nixus on 03.11.2017.
 */

public class FeatureTypes extends ListResponse<FeatureInfo> {
    public FeatureTypes(@NonNull List<FeatureInfo> models) {
        super(models);
    }
}