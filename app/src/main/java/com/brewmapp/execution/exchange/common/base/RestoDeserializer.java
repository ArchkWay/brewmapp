package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.RestoInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 21.10.2017.
 */

public class RestoDeserializer extends AdapterItemDeserializer<Resto, RestoInfo> {
    @Override
    protected Class<Resto> provideType() {
        return Resto.class;
    }

    @Override
    protected Class<RestoInfo> provideWrapperType() {
        return RestoInfo.class;
    }
}
