package com.brewmapp.execution.task.containers.contract;

import com.brewmapp.execution.exchange.response.base.MessageResponse;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by xpusher on 11/14/2017.
 */

public interface ContainerTasks {
    void clickLikeDislike(String relatedModel, int relatedId, int type_like, SimpleSubscriber<MessageResponse> simpleSubscriber);

    void loadInteres(String capBeer, Integer integer, SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber);

    void interestOFF(String id_interest, SimpleSubscriber<String> simpleSubscriber);

    void interestON(String relatedModel, String relatedId, SimpleSubscriber<String> simpleSubscriber);
}
