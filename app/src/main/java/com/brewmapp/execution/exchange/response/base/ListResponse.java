package com.brewmapp.execution.exchange.response.base;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by oleg on 10.08.17.
 */

public class ListResponse<T> {
    private int total;
    private List<T> models;

    public ListResponse(@NonNull List<T> models) {
        this.models = models;
        this.total = models.size();
    }

    public int getTotal() {
        return total;
    }

    public List<T> getModels() {
        return models;
    }
}
