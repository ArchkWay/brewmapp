package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerDensity;
import com.brewmapp.data.entity.BeerDensityTypes;
import com.brewmapp.data.entity.wrapper.BeerDensityInfo;
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

public class BeerDensityTask extends BaseNetworkTask<BeerDensity, List<IFlexible>> {

    @Inject
    public BeerDensityTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerDensity beerDensity) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("ProductDensity");
                params.addParam("id", "");    //it's backend style :)
                BeerDensityTypes response = executeCall(getApi().loadBeerDensity(params));
                List<BeerDensityInfo> beerDensityInfos = new ArrayList<>();
                beerDensityInfos.add(0, new BeerDensityInfo(new BeerDensity("Любое  ")));
                beerDensityInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(beerDensityInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
