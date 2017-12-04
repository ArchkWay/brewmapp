package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerIbu;
import com.brewmapp.data.entity.wrapper.BeerIbuInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerIbuDeserializer extends AdapterItemDeserializer<BeerIbu, BeerIbuInfo> {

    @Override
    protected Class<BeerIbu> provideType() {
        return BeerIbu.class;
    }

    @Override
    protected Class<BeerIbuInfo> provideWrapperType() {
        return BeerIbuInfo.class;
    }
}
