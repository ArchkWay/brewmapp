package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.BeerColor;
import com.brewmapp.data.entity.wrapper.BeerColorInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerColorDeserializer extends AdapterItemDeserializer<BeerColor, BeerColorInfo> {
    @Override
    protected Class<BeerColor> provideType() {
        return BeerColor.class;
    }

    @Override
    protected Class<BeerColorInfo> provideWrapperType() {
        return BeerColorInfo.class;
    }
}
