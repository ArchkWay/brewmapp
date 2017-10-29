package com.brewmapp.execution.task;

import com.brewmapp.data.entity.RestoLocation;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nlbochas on 21/10/2017.
 */

public class LoadRestoLocationTask extends BaseNetworkTask<Integer, List<RestoLocation>> {

    @Inject
    public LoadRestoLocationTask(MainThread mainThread,
                                 Executor executor,
                                 Api api ) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<RestoLocation>> prepareObservable(Integer params) {
        return Observable.create(subscriber -> {
            try {
                ListResponse<RestoLocation> response = executeCall(getApi().loadRestoLocationInCity(params));
                subscriber.onNext(response.getModels());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
