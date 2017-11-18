package com.brewmapp.execution.task;

import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.data.pojo.FilterRestoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nlbochas on 18/11/2017.
 */

public class FilterRestoTask extends BaseNetworkTask<FilterRestoPackage, List<FilterRestoLocation>> {

    @Inject
    public FilterRestoTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<FilterRestoLocation>> prepareObservable(FilterRestoPackage restoPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams wrapperParams = new WrapperParams(Wrappers.RESTO_LOCATION);
//                wrapperParams.addParam(Keys.NAME, params.getName());
                wrapperParams.addParam(Keys.RESTO_DISCOUNT, restoPackage.getResto_discount());
                subscriber.onNext(executeCall(getApi().loadRestoLocation(wrapperParams)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
