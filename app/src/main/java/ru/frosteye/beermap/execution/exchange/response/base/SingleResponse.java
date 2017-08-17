package ru.frosteye.beermap.execution.exchange.response.base;

import ru.frosteye.beermap.data.entity.Album;
import ru.frosteye.beermap.data.entity.User;

/**
 * Created by ovcst on 02.08.2017.
 */

public class SingleResponse<T> {
    private T models;

    public T getData() {
        return models;
    }
}
