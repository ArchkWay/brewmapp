package com.brewmapp.execution.exchange.response.base;

/**
 * Created by ovcst on 02.08.2017.
 */

public class SingleResponse<T> {
    private T models;

    public T getData() {
        return models;
    }
}
