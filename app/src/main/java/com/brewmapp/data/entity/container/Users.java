package com.brewmapp.data.entity.container;

import android.support.annotation.NonNull;

import com.brewmapp.data.entity.wrapper.UserInfo;
import com.brewmapp.execution.exchange.response.base.ListResponse;

import java.util.List;

/**
 * Created by xpusher on 11/28/2017.
 */

public class Users extends ListResponse<UserInfo> {
    public Users(@NonNull List<UserInfo> models) {
        super(models);
    }
}
