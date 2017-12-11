package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BreweryShort;
import com.brewmapp.data.entity.BreweryTypes;
import com.brewmapp.data.entity.wrapper.BreweryInfo;
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
 * Created by nixus on 05.12.2017.
 */

public class BreweryTask extends BaseNetworkTask<BreweryShort, List<IFlexible>> {

    @Inject
    public BreweryTask(MainThread mainThread,
                           Executor executor,
                           Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(BreweryShort breweryShort) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams("Brewery");
                params.addParam("id", "");    //it's backend style :)
                params.addParam("country_id", "");
                params.addParam("location_id", "");
                BreweryTypes response = executeCall(getApi().loadBrewery(params));
                List<BreweryInfo> breweryInfos = new ArrayList<>();
                breweryInfos.add(0, new BreweryInfo(new BreweryShort("Не имеет значения  ")));
                breweryInfos.addAll(response.getModels());
                subscriber.onNext(new ArrayList<>(breweryInfos));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
