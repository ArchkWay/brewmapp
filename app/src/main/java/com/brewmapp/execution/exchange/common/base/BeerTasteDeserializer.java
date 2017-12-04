package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerTaste;
import com.brewmapp.data.entity.wrapper.BeerTasteInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerTasteDeserializer extends AdapterItemTempDeserializer<BeerTaste, BeerTasteInfo> {
    @Override
    protected Class<BeerTaste> provideType() {
        return BeerTaste.class;
    }

    @Override
    protected Class<BeerTasteInfo> provideWrapperType() {
        return BeerTasteInfo.class;
    }
}
