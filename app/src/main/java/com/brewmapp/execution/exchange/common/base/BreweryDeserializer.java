package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.wrapper.BreweryInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 05.12.2017.
 */

public class BreweryDeserializer extends AdapterItemTempDeserializer<Brewery, BreweryInfo> {
    @Override
    protected Class<Brewery> provideType() {
        return Brewery.class;
    }

    @Override
    protected Class<BreweryInfo> provideWrapperType() {
        return BreweryInfo.class;
    }
}
