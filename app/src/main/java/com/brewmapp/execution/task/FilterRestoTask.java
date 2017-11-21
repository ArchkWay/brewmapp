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
import ru.frosteye.ovsa.execution.network.request.RequestParams;

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
                RequestParams requestParams = new RequestParams();
                requestParams.addParam(Keys.RESTO_CITY, restoPackage.getRestoCity());
                requestParams.addParam(Keys.MENU_BEER, restoPackage.getMenuBeer());
                requestParams.addParam(Keys.RESTO_TYPE, restoPackage.getRestoTypes());
                requestParams.addParam(Keys.RESTO_FEATURES, restoPackage.getRestoFeatures());
                requestParams.addParam(Keys.RESTO_AVERAGE, restoPackage.getRestoPrices());
                requestParams.addParam(Keys.RESTO_KITCHEN, restoPackage.getRestoKitchens());
                requestParams.addParam(Keys.RESTO_DISCOUNT, restoPackage.getResto_discount());
                subscriber.onNext(executeCall(getApi().loadRestoLocation(requestParams)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
