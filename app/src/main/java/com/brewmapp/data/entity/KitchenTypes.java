package com.brewmapp.data.entity;

import com.brewmapp.data.entity.wrapper.KitchenInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by nixus on 02.11.2017.
 */

public class KitchenTypes extends ListResponse<KitchenInfo> {
    public KitchenTypes(@NonNull List<KitchenInfo> models) {
        super(models);
    }
}