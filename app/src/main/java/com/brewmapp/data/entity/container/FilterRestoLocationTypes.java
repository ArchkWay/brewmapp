package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 04.12.2017.
 */

public class FilterRestoLocationTypes extends ListResponse<FilterRestoLocationInfo> {
    public FilterRestoLocationTypes(@NonNull List<FilterRestoLocationInfo> models) {
        super(models);
    }
}
