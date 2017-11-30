package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerColor;
import com.brewmapp.data.entity.BeerColorTypes;
import com.brewmapp.data.entity.BeerTaste;
import com.brewmapp.data.entity.BeerTasteTypes;
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

public class BeerTasteTask extends BaseNetworkTask<BeerTaste, List<IFlexible>> {

    @Inject
    public BeerTasteTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerTaste beerPack) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("BeerTaste");
                params.addParam("id", "");    //it's backend style :)
                BeerTasteTypes response = executeCall(getApi().loadBeerTaste(params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}

