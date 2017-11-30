package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerPower;
import com.brewmapp.data.entity.BeerPowerTypes;
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

public class BeerPowerTask extends BaseNetworkTask<BeerPower, List<IFlexible>> {

    @Inject
    public BeerPowerTask(MainThread mainThread,
                              Executor executor,
                              Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerPower beerPower) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("BeerStrength");
                params.addParam("id", "");    //it's backend style :)
                BeerPowerTypes response = executeCall(getApi().loadBeerPower(params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}

