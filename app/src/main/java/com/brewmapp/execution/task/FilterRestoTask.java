package com.brewmapp.execution.task;

import com.brewmapp.data.entity.FilterRestoLocation;
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
                RequestParams params = new RequestParams();
                if(restoPackage.getRestoCity()!=null)
                    params.addParam(Keys.RESTO_CITY, restoPackage.getRestoCity());
//                params.addParam(Keys.MENU_BEER, restoPackage.getMenuBeer() != null ? restoPackage.getMenuBeer() : "");
//                params.addParam(Keys.RESTO_TYPE, restoPackage.getRestoTypes() != null ? restoPackage.getRestoTypes() : "");
//                params.addParam(Keys.RESTO_FEATURES, restoPackage.getRestoFeatures() != null ? restoPackage.getRestoFeatures() : "");
//                params.addParam(Keys.RESTO_AVERAGE, restoPackage.getRestoPrices() != null ? restoPackage.getRestoPrices() : "");
//                params.addParam(Keys.RESTO_KITCHEN, restoPackage.getRestoKitchens() != null ? restoPackage.getRestoKitchens() : "");
//                params.addParam(Keys.RESTO_DISCOUNT, restoPackage.getResto_discount());
//
//                params.addParam(Keys.COORD_START , restoPackage.getCoordStart() != null ? restoPackage.getCoordStart() : "");
//                params.addParam(Keys.COORD_END , restoPackage.getCoordEnd() != null ? restoPackage.getCoordEnd() : "");

                ListResponse<FilterRestoLocation> response = executeCall(getApi().loadRestoLocation(params));
                subscriber.onNext(response.getModels());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
