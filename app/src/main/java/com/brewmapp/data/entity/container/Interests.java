package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.InterestInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 22.10.2017.
 */

public class Interests  extends ListResponse<InterestInfo> {
    public Interests(@NonNull List<InterestInfo> models) {
        super(models);
    }
}
