package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerPack;
import com.brewmapp.data.entity.BeerPackTypes;
import com.brewmapp.data.entity.BeerTypesModel;
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
 * Created by nixus on 28.11.2017.
 */

public class BeerPackTask extends BaseNetworkTask<BeerPack, List<IFlexible>> {

    @Inject
    public BeerPackTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerPack beerPack) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("");
                BeerPackTypes response = executeCall(getApi().loadBeerPack(params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
