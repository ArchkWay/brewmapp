package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.SearchBeer;
import com.brewmapp.data.entity.wrapper.SearchBeerInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 07.12.2017.
 */

public class SearchBeerDeserializer extends AdapterItemDeserializer<SearchBeer, SearchBeerInfo> {
    @Override
    protected Class<SearchBeer> provideType() {
        return SearchBeer.class;
    }

    @Override
    protected Class<SearchBeerInfo> provideWrapperType() {
        return SearchBeerInfo.class;
    }
}
