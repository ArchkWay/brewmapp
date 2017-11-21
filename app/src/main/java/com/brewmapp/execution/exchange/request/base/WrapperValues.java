package com.brewmapp.execution.exchange.request.base;

import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by Kras on 16.11.2017.
 */

public class WrapperValues extends RequestParams {

    public RequestParams addValue(String key, Object value) {
        return super.addParam(key, value);
    }

}
