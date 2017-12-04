package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Region;
import com.brewmapp.data.entity.wrapper.RegionInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by nixus on 02.12.2017.
 */

public class RegionDeserializer extends AdapterItemDeserializer<Region, RegionInfo> {
    @Override
    protected Class<Region> provideType() {
        return Region.class;
    }

    @Override
    protected Class<RegionInfo> provideWrapperType() {
        return RegionInfo.class;
    }
}
