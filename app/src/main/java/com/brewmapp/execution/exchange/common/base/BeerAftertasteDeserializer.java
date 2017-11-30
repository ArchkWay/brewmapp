package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerAftertaste;
import com.brewmapp.data.entity.wrapper.BeerAftertasteInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerAftertasteDeserializer extends AdapterItemTempDeserializer<BeerAftertaste, BeerAftertasteInfo> {
    @Override
    protected Class<BeerAftertaste> provideType() {
        return BeerAftertaste.class;
    }

    @Override
    protected Class<BeerAftertasteInfo> provideWrapperType() {
        return BeerAftertasteInfo.class;
    }
}
