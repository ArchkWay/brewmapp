package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.CountryTypes;
import com.brewmapp.data.entity.Region;
import com.brewmapp.data.entity.RegionTypes;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
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
 * Created by nixus on 02.12.2017.
 */

public class RegionTask extends BaseNetworkTask<GeoPackage, List<IFlexible>> {

    @Inject
    public RegionTask(MainThread mainThread,
                       Executor executor,
                       Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(GeoPackage geoPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams wrapperParams = new WrapperParams("Region");
                wrapperParams.addParam("country_id", geoPackage.getCountryId());
                RegionTypes response = executeCall(getApi().loadRegions(wrapperParams));
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
