package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.data.entity.wrapper.InterestInfoByUsers;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 22.10.2017.
 */

public class InterestsByUser extends ListResponse<InterestInfoByUsers> {
    public InterestsByUser(@NonNull List<InterestInfoByUsers> models) {
        super(models);
    }
}
