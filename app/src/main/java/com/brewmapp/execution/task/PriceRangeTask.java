package com.brewmapp.execution.task;

import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.PriceRangeTypes;

import com.brewmapp.data.entity.wrapper.PriceRangeInfo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 03.11.2017.
 */

public class PriceRangeTask extends BaseNetworkTask<PriceRange, List<IFlexible>> {

    @Inject
    public PriceRangeTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(PriceRange priceRange) {
        return Observable.create(subscriber -> {
            try {
                PriceRangeTypes response = executeCall(getApi().loadPriceRanges());
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
