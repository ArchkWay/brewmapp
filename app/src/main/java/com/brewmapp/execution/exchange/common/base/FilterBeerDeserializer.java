package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.wrapper.FilterBeerInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nlbochas on 17/11/2017.
 */

public class FilterBeerDeserializer extends AdapterItemDeserializer<Beer, FilterBeerInfo> {
    @Override
    protected Class<Beer> provideType() {
        return Beer.class;
    }

    @Override
    protected Class<FilterBeerInfo> provideWrapperType() {
        return FilterBeerInfo.class;
    }
}
