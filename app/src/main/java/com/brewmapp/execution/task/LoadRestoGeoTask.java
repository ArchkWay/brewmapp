package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.data.pojo.RestoGeoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperValues;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 16.11.2017.
 */

public class LoadRestoGeoTask extends BaseNetworkTask<RestoGeoPackage,Object> {

    @Inject
    public LoadRestoGeoTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<Object> prepareObservable(RestoGeoPackage restoGeoPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperValues params = new WrapperValues();
                if(restoGeoPackage.getBeer_id()!=null)
                    params.addValue(Keys.menuBeer,restoGeoPackage.getBeer_id());
                if(restoGeoPackage.getCoordStart()!=null)
                    params.addValue(Keys.CoordStart,restoGeoPackage.getCoordStart());
                if(restoGeoPackage.getCoordEnd()!=null)
                    params.addValue(Keys.CoordEnd,restoGeoPackage.getCoordEnd());

                ListResponse<RestoLocation> o=executeCall(getApi().loadRestoGeo(params));
                subscriber.onNext("");
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }
}
