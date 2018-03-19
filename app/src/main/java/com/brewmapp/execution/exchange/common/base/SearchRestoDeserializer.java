package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.wrapper.SearchRestoInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 21.10.2017.
 */

public class SearchRestoDeserializer extends AdapterItemDeserializer<Resto, SearchRestoInfo> {
    @Override
    protected Class<Resto> provideType() {
        return Resto.class;
    }

    @Override
    protected Class<SearchRestoInfo> provideWrapperType() {
        return SearchRestoInfo.class;
    }
}
