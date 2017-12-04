package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.CityInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 02.12.2017.
 */

public class CityTypes extends ListResponse<CityInfo> {
    public CityTypes(@NonNull List<CityInfo> models) {
        super(models);
    }
}