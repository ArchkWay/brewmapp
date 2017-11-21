package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.LikesByBeerPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 16.11.2017.
 */

public class LoadLikesByBeerTask extends BaseNetworkTask<LikesByBeerPackage,Object> {

    @Inject
    public LoadLikesByBeerTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<Object> prepareObservable(LikesByBeerPackage likesByBeerPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.LIKE);
                params.addParam(Keys.RELATED_MODEL,likesByBeerPackage.getRelated_model());
                params.addParam(Keys.RELATED_ID,likesByBeerPackage.getRelated_id());
                Object o=executeCall(getApi().loadLikesByBeer(params));
                subscriber.onNext("");
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });

    }
}
