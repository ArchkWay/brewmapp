package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.RegionInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 02.12.2017.
 */

public class RegionTypes extends ListResponse<RegionInfo> {
    public RegionTypes(@NonNull List<RegionInfo> models) {
        super(models);
    }
}
