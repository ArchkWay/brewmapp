package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.data.pojo.BeerTypes;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerTypesDeserializer extends AdapterItemTempDeserializer<BeerTypes, BeerTypeInfo> {
    @Override
    protected Class<BeerTypes> provideType() {
        return BeerTypes.class;
    }

    @Override
    protected Class<BeerTypeInfo> provideWrapperType() {
        return BeerTypeInfo.class;
    }
}
