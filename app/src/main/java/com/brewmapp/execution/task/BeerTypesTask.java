package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerTypesModel;
import com.brewmapp.data.entity.wrapper.BeerTypeInfo;
import com.brewmapp.data.pojo.BeerTypes;
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
 * Created by nixus on 27.11.2017.
 */

public class BeerTypesTask extends BaseNetworkTask<BeerTypes, List<IFlexible>> {

    @Inject
    public BeerTypesTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerTypes beerTypes) {
        return Observable.create(subscriber -> {
            try {

                WrapperParams params = new WrapperParams("");
                BeerTypesModel response = executeCall(getApi().loadBeerTypes(params));
                List<BeerTypeInfo> beerTypeInfos = new ArrayList<>();
                beerTypeInfos.add(0, new BeerTypeInfo(new BeerTypes("Любой  ")));
                beerTypeInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(beerTypeInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
