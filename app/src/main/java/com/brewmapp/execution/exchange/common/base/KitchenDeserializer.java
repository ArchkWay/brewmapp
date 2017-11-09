package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Kitchen;
import com.brewmapp.data.entity.wrapper.KitchenInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 02.11.2017.
 */

public class KitchenDeserializer extends AdapterItemDeserializer<Kitchen, KitchenInfo> {
    @Override
    protected Class<Kitchen> provideType() {
        return Kitchen.class;
    }

    @Override
    protected Class<KitchenInfo> provideWrapperType() {
        return KitchenInfo.class;
    }
}
