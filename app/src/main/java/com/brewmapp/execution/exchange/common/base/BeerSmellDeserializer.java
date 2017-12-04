package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerSmell;
import com.brewmapp.data.entity.wrapper.BeerSmellInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerSmellDeserializer extends AdapterItemDeserializer<BeerSmell, BeerSmellInfo> {

    @Override
    protected Class<BeerSmell> provideType() {
        return BeerSmell.class;
    }

    @Override
    protected Class<BeerSmellInfo> provideWrapperType() {
        return BeerSmellInfo.class;
    }
}
