package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerColor;
import com.brewmapp.data.entity.BeerColorTypes;
import com.brewmapp.data.entity.BeerPack;
import com.brewmapp.data.entity.BeerPackTypes;
import com.brewmapp.data.entity.wrapper.BeerColorInfo;
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

public class BeerColorTask extends BaseNetworkTask<BeerColor, List<IFlexible>> {

    @Inject
    public BeerColorTask(MainThread mainThread,
                        Executor executor,
                        Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BeerColor beerPack) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("BeerColor");
                params.addParam("id", "");    //it's backend style :)
                BeerColorTypes response = executeCall(getApi().loadBeerColors(params));
                List<BeerColorInfo> beerColorInfos = new ArrayList<>();
                beerColorInfos.add(0, new BeerColorInfo(new BeerColor("Любой  ")));
                beerColorInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(beerColorInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
