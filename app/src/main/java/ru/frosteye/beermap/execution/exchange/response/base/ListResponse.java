package ru.frosteye.beermap.execution.exchange.response.base;

import java.util.List;

/**
 * Created by oleg on 10.08.17.
 */

public class ListResponse<T> {
    private int total;
    private List<T> models;

    public int getTotal() {
        return total;
    }

    public List<T> getModels() {
        return models;
    }
}
