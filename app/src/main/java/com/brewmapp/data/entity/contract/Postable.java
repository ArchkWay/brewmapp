package com.brewmapp.data.entity.contract;

import com.brewmapp.execution.exchange.request.base.WrapperParams;

/**
 * Created by oleg on 17.08.17.
 */

public interface Postable {
    WrapperParams createParams();
}
