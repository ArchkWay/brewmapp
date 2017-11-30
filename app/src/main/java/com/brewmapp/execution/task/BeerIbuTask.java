package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerDensity;
import com.brewmapp.data.entity.BeerDensityTypes;
import com.brewmapp.data.entity.BeerIbu;
import com.brewmapp.data.entity.BeerIbuTypes;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerIbuTask extends BaseNetworkTask<BeerIbu, List<IFlexible>> {

    @Inject
    public BeerIbuTask(MainThread mainThread,
                           Executor executor,
                           Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerIbu beerIbu) {
        return Observable.create(subscriber -> {
            try {
                BeerIbuTypes response = executeCall(getApi().loadBeerIbu());
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
