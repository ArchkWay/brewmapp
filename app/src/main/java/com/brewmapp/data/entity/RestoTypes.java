package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.RestoTypeInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 01.11.2017.
 */

public class RestoTypes extends ListResponse<RestoTypeInfo> {
    public RestoTypes(@NonNull List<RestoTypeInfo> models) {
        super(models);
    }
}