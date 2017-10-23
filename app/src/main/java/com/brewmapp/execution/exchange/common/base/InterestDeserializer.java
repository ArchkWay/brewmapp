package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.wrapper.InterestInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestDeserializer extends AdapterItemDeserializer<Interest, InterestInfo> {
    @Override
    protected Class<Interest> provideType() {
        return Interest.class;
    }

    @Override
    protected Class<InterestInfo> provideWrapperType() {
        return InterestInfo.class;
    }
}
