package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.BreweriesInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 16.12.2017.
 */

public class Breweries extends ListResponse<BreweriesInfo> {
    public Breweries(@NonNull List<BreweriesInfo> models) {
        super(models);
    }
}
