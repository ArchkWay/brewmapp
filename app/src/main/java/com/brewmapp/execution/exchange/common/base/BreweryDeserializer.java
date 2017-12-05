package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BreweryShort;
import com.brewmapp.data.entity.wrapper.BreweryInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 05.12.2017.
 */

public class BreweryDeserializer extends AdapterItemTempDeserializer<BreweryShort, BreweryInfo> {
    @Override
    protected Class<BreweryShort> provideType() {
        return BreweryShort.class;
    }

    @Override
    protected Class<BreweryInfo> provideWrapperType() {
        return BreweryInfo.class;
    }
}
