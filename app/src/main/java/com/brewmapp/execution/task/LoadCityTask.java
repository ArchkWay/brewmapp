package com.brewmapp.execution.task;

import com.brewmapp.data.entity.City;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

public class LoadCityTask extends BaseNetworkTask<GeoPackage, List<City>> {

    @Inject
    public LoadCityTask(MainThread mainThread,
                        Executor executor,
                        Api api ) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<City>> prepareObservable(GeoPackage geoPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams wrapperParams = new WrapperParams("");
                ListResponse<City> response = executeCall(getApi().loadCity(wrapperParams));
                if (response.getModels().size() > 0) {
                    subscriber.onNext(response.getModels());
                }
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
