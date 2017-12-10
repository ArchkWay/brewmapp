package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.SearchBeerInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by nixus on 07.12.2017.
 */

public class SearchBeerTypes extends ListResponse<SearchBeerInfo> {
    public SearchBeerTypes(@NonNull List<SearchBeerInfo> models) {
        super(models);
    }
}
