package ru.frosteye.beermap.data.entity.contract;

import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;

/**
 * Created by oleg on 17.08.17.
 */

public interface Postable {
    WrapperParams createParams();
}
