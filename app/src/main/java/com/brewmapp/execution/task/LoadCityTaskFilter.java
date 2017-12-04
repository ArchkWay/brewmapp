package com.brewmapp.execution.task;

import com.brewmapp.data.entity.CityTypes;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 02.12.2017.
 */

public class LoadCityTaskFilter extends BaseNetworkTask<GeoPackage, List<IFlexible>> {

    @Inject
    public LoadCityTaskFilter(MainThread mainThread,
                        Executor executor,
                        Api api ) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(GeoPackage geoPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.CITY);
                params.addParam(Keys.NAME, geoPackage.getCityName() != null ? geoPackage.getCityName() : "");
                params.addParam(Keys.COUNTRY_ID, geoPackage.getCountryId() != null ? geoPackage.getCountryId() : "");
                params.addParam(Keys.REGION_ID, geoPackage.getRegionId() != null ? geoPackage.getRegionId() : "");
                CityTypes response = executeCall(getApi().loadCityFilter(params));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}

