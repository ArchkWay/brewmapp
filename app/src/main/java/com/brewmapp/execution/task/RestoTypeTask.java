package com.brewmapp.execution.task;

import com.brewmapp.data.entity.RestoType;
import com.brewmapp.data.entity.RestoTypes;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by nixus on 01.11.2017.
 */

public class RestoTypeTask extends BaseNetworkTask<RestoType, List<IFlexible>> {

    @Inject
    public RestoTypeTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(RestoType restoType) {
        return Observable.create(subscriber -> {
            try {
                RestoTypes response = executeCall(getApi().loadRestoTypes());
                subscriber.onNext(new ArrayList<>(response.getModels()));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
