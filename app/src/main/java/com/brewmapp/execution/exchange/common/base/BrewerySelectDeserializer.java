package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BreweryShort;
import com.brewmapp.data.entity.wrapper.BreweryInfoSelect;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 05.12.2017.
 */

public class BrewerySelectDeserializer extends AdapterItemTempDeserializer<BreweryShort, BreweryInfoSelect> {
    @Override
    protected Class<BreweryShort> provideType() {
        return BreweryShort.class;
    }

    @Override
    protected Class<BreweryInfoSelect> provideWrapperType() {
        return BreweryInfoSelect.class;
    }
}
