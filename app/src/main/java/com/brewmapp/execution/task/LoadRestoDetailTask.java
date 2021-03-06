package com.brewmapp.execution.task;


import android.util.Log;

import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.container.RestoDetails;
import com.brewmapp.data.pojo.LoadRestoDetailPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 26.10.2017.
 */

public class LoadRestoDetailTask extends BaseNetworkTask<LoadRestoDetailPackage, RestoDetail>  {

    @Inject
    public LoadRestoDetailTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<RestoDetail> prepareObservable(LoadRestoDetailPackage loadRestoDetailPackage) {
        return Observable.create(subscriber -> {
            try {

                WrapperParams wrapperParams=new WrapperParams("");
                RestoDetails restoDetails;
                if(loadRestoDetailPackage.getLat()!=0&&loadRestoDetailPackage.getLon()!=0) {
                    restoDetails = executeCall(getApi().getRestoDetailsWithDistance(loadRestoDetailPackage.getId(), wrapperParams,loadRestoDetailPackage.getLat(),loadRestoDetailPackage.getLon(),loadRestoDetailPackage.getNoMenu()));
                }else {
                    restoDetails = executeCall(getApi().getRestoDetails(loadRestoDetailPackage.getId(), wrapperParams));
                }
                subscriber.onNext((RestoDetail) restoDetails.getModels().get(0).getModel());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
