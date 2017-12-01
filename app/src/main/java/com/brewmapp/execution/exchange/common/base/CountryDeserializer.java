package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.wrapper.CountryInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 02.12.2017.
 */

public class CountryDeserializer extends AdapterItemTempDeserializer<Country, CountryInfo> {
    @Override
    protected Class<Country> provideType() {
        return Country.class;
    }

    @Override
    protected Class<CountryInfo> provideWrapperType() {
        return CountryInfo.class;
    }
}
