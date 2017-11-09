package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.RestoType;
import com.brewmapp.data.entity.wrapper.RestoTypeInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 02.11.2017.
 */

public class RestoTypeDeserializer extends AdapterItemDeserializer<RestoType, RestoTypeInfo> {
    @Override
    protected Class<RestoType> provideType() {
        return RestoType.class;
    }

    @Override
    protected Class<RestoTypeInfo> provideWrapperType() {
        return RestoTypeInfo.class;
    }
}
