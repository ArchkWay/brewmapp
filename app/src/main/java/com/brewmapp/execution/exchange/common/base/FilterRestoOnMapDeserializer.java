package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.FilterRestoOnMap;
import com.brewmapp.data.entity.wrapper.FilterRestoLocationInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 04.12.2017.
 */

public class FilterRestoOnMapDeserializer extends AdapterItemDeserializer<FilterRestoOnMap, FilterRestoLocationInfo> {
    @Override
    protected Class<FilterRestoOnMap> provideType() {
        return FilterRestoOnMap.class;
    }

    @Override
    protected Class<FilterRestoLocationInfo> provideWrapperType() {
        return FilterRestoLocationInfo.class;
    }
}