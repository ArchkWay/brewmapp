package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.EventInfo;
import com.brewmapp.data.entity.wrapper.PostInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by oleg on 17.08.17.
 */

public class Events extends ListResponse<EventInfo> {

    public Events(@NonNull List<EventInfo> models) {
        super(models);
    }
}
