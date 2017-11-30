package com.brewmapp.execution.task;

import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.entity.FilterBeerLocation;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterBeerPackage;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by nixus on 30.11.2017.
 */

public class FilterBeerCoordinates  extends BaseNetworkTask<FilterBeerPackage, List<FilterBeerLocation>> {

    @Inject
    public FilterBeerCoordinates(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<FilterBeerLocation>> prepareObservable(FilterBeerPackage beerPackage) {
        return Observable.create(subscriber -> {
            try {
                RequestParams requestParams = new RequestParams();
                ListResponse<FilterBeerLocation> beerLocation = executeCall(getApi().loadBeerLocation(requestParams));
                if (beerLocation.getModels().size() > 0) {
                    subscriber.onNext(beerLocation.getModels());
                }
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
