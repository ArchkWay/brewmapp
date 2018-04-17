package com.brewmapp.execution.exchange.request.base;

import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 26.07.17.
 */

public class WrapperParams extends RequestParams {

    private String wrapper;

    public WrapperParams(String wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public RequestParams addParam(String key, Object value) {
        return super.addParam(createKey(key), value);
    }

    public String createKey(String key){
        return wrapper + "[" + key + "]";
    }
}
