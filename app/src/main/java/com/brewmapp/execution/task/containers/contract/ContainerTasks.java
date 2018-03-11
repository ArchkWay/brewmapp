package com.brewmapp.execution.task.containers.contract;

import com.brewmapp.data.entity.Averagevalue;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.pojo.LoadAverageValuePackage;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.data.pojo.SearchPackage;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by xpusher on 11/14/2017.
 */

public interface ContainerTasks {
    void clickLikeDislike(String relatedModel, int relatedId, int type_like, SimpleSubscriber<MessageResponse> simpleSubscriber);

    void loadInteres(LoadInterestPackage loadInterestPackage, SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber);

    void loadInteresByUsers(String related_model, Integer related_id, SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber);

    void interestOFF(String id_interest, SimpleSubscriber<String> simpleSubscriber);

    void interestON(String relatedModel, String relatedId, SimpleSubscriber<String> simpleSubscriber);

    void loadReviewsTask(ReviewPackage reviewPackage, SimpleSubscriber<List<IFlexible>> simpleSubscriber);

    void addReviewTask(String relatedModel, String relatedId, String text, int type, SimpleSubscriber<String> simpleSubscriber);

    void loadRestoByBeer(SearchPackage searchPackage, SimpleSubscriber<ListResponse<Resto>> objectSimpleSubscriber);

    void loadLikesByBeer(String beer_id, SimpleSubscriber<Object> simpleSubscriber);

    void loadUsersByInteres(Integer interest_id, SimpleSubscriber<List<IFlexible>> objectSimpleSubscriber);

    void loadProductAverageValue(LoadAverageValuePackage loadAverageValuePackage, SimpleSubscriber<ListResponse<Averagevalue>> listResponseSimpleSubscriber);

    void cancelTasks();
}
