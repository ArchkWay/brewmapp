package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.wrapper.PriceRangeInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 03.11.2017.
 */

public class PriceRangeDeserializer extends AdapterItemDeserializer<PriceRange, PriceRangeInfo> {

    @Override
    protected Class<PriceRange> provideType() {
        return PriceRange.class;
    }

    @Override
    protected Class<PriceRangeInfo> provideWrapperType() {
        return PriceRangeInfo.class;
    }
}
