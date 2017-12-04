package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.wrapper.CityInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemTempDeserializer;

/**
 * Created by nixus on 02.12.2017.
 */

public class CityDeserializer extends AdapterItemTempDeserializer<City, CityInfo> {
    @Override
    protected Class<City> provideType() {
        return City.class;
    }

    @Override
    protected Class<CityInfo> provideWrapperType() {
        return CityInfo.class;
    }
}
