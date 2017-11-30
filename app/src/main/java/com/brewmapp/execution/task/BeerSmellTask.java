package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerSmell;
import com.brewmapp.data.entity.BeerSmellTypes;
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

public class BeerSmellTask extends BaseNetworkTask<BeerSmell, List<IFlexible>> {

    @Inject
    public BeerSmellTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerSmell beerPack) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("BeerFragrance");
                params.addParam("id", "");    //it's backend style :)
                BeerSmellTypes response = executeCall(getApi().loadBeerSmell(params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}

