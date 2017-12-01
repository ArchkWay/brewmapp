package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.CountryInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 02.12.2017.
 */

public class CountryTypes extends ListResponse<CountryInfo> {
    public CountryTypes(@NonNull List<CountryInfo> models) {
        super(models);
    }
}