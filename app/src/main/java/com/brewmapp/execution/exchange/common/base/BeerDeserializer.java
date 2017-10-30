package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.wrapper.BeerInfo;

import java.io.Serializable;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 21.10.2017.
 */

public class BeerDeserializer extends AdapterItemDeserializer<Beer, BeerInfo> {
    @Override
    protected Class<Beer> provideType() {
        return Beer.class;
    }

    @Override
    protected Class<BeerInfo> provideWrapperType() {
        return BeerInfo.class;
    }
}
