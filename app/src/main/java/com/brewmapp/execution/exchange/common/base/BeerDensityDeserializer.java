package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerDensity;
import com.brewmapp.data.entity.wrapper.BeerDensityInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerDensityDeserializer extends AdapterItemDeserializer<BeerDensity, BeerDensityInfo> {
    @Override
    protected Class<BeerDensity> provideType() {
        return BeerDensity.class;
    }

    @Override
    protected Class<BeerDensityInfo> provideWrapperType() {
        return BeerDensityInfo.class;
    }
}
