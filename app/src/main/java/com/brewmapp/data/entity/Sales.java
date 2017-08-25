package com.brewmapp.data.entity;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.SaleInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by ovcst on 25.08.2017.
 */

public class Sales extends ListResponse<SaleInfo> {
    public Sales(@NonNull List<SaleInfo> models) {
        super(models);
    }
}
