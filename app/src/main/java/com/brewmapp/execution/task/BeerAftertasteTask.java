package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerAftertaste;
import com.brewmapp.data.entity.BeerAftertasteTypes;
import com.brewmapp.data.entity.wrapper.BeerAftertasteInfo;
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

public class BeerAftertasteTask extends BaseNetworkTask<BeerAftertaste, List<IFlexible>> {

    @Inject
    public BeerAftertasteTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerAftertaste beerPack) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("BeerAftertaste");
                params.addParam("id", "");    //it's backend style :)
                BeerAftertasteTypes response = executeCall(getApi().loadBeerAfterTaste(params));
                List<BeerAftertasteInfo> beerAftertasteInfos = new ArrayList<>();
                beerAftertasteInfos.add(0, new BeerAftertasteInfo(new BeerAftertaste("Любое  ")));
                beerAftertasteInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(beerAftertasteInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
