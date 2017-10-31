package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.PostInfo;
import com.brewmapp.data.entity.wrapper.SubscriptionInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 31.10.2017.
 */

public class Subscriptions extends ListResponse<SubscriptionInfo> {
    public Subscriptions(@NonNull List<SubscriptionInfo> models) {
        super(models);
    }
}
