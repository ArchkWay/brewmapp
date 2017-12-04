package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerPower;
import com.brewmapp.data.entity.wrapper.BeerPowerInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerPowerDeserializer extends AdapterItemDeserializer<BeerPower, BeerPowerInfo> {

    @Override
    protected Class<BeerPower> provideType() {
        return BeerPower.class;
    }

    @Override
    protected Class<BeerPowerInfo> provideWrapperType() {
        return BeerPowerInfo.class;
    }
}
