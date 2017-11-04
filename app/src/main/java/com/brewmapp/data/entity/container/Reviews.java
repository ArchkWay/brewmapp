package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.ReviewInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by Kras on 04.11.2017.
 */

public class Reviews extends ListResponse<ReviewInfo> {

    public Reviews(@NonNull List<ReviewInfo> models) {
        super(models);
    }
}
