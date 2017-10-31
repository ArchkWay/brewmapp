package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.presentation.view.impl.widget.SubscriptionView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by Kras on 31.10.2017.
 */

public class SubscriptionInfo extends AdapterItem<Subscription, SubscriptionView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_subscription;
    }
}
