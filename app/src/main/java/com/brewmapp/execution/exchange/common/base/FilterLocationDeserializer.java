package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.FilterOnMapResto;
import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 04.12.2017.
 */

public class FilterLocationDeserializer extends AdapterItemDeserializer<FilterOnMapResto, FilterRestoLocationInfo> {
    @Override
    protected Class<FilterOnMapResto> provideType() {
        return FilterOnMapResto.class;
    }

    @Override
    protected Class<FilterRestoLocationInfo> provideWrapperType() {
        return FilterRestoLocationInfo.class;
    }
}
