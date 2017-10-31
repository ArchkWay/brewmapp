package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.wrapper.SubscriptionInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 31.10.2017.
 */

public class SubscriptionDeserializer extends AdapterItemDeserializer<Subscription, SubscriptionInfo> {
    @Override
    protected Class<Subscription> provideType() {
        return Subscription.class;
    }

    @Override
    protected Class<SubscriptionInfo> provideWrapperType() {
        return SubscriptionInfo.class;
    }
}
