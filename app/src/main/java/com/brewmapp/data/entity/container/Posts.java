package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import java.util.List;

import com.brewmapp.data.entity.wrapper.PostInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

/**
 * Created by oleg on 17.08.17.
 */

public class Posts extends ListResponse<PostInfo> {

    public Posts(@NonNull List<PostInfo> models) {
        super(models);
    }
}
