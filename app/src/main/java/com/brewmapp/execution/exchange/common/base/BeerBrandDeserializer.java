package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerBrandDeserializer extends AdapterItemDeserializer<BeerBrand, BeerBrandInfo> {
    @Override
    protected Class<BeerBrand> provideType() {
        return BeerBrand.class;
    }

    @Override
    protected Class<BeerBrandInfo> provideWrapperType() {
        return BeerBrandInfo.class;
    }
}