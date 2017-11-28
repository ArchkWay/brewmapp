package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerPack;
import com.brewmapp.data.entity.wrapper.BeerPackInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerPackDeserializer extends AdapterItemDeserializer<BeerPack, BeerPackInfo> {
    @Override
    protected Class<BeerPack> provideType() {
        return BeerPack.class;
    }

    @Override
    protected Class<BeerPackInfo> provideWrapperType() {
        return BeerPackInfo.class;
    }
}
