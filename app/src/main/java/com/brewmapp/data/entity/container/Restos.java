package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.RestoInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 25.10.2017.
 */

public class Restos extends ListResponse<RestoInfo> {
    public Restos(@NonNull List<RestoInfo> models) {
        super(models);
    }
}
