package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.BeerLocation;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadLocationTask extends BaseNetworkTask<Integer, BeerLocation.LocationInfo> {


    @Inject
    public LoadLocationTask(MainThread mainThread,
                            Executor executor,
                            Api api ) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<BeerLocation.LocationInfo> prepareObservable(Integer params) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params1 = new WrapperParams(Wrappers.LOCATION);
                params1.addParam(Keys.ID, params);
                ListResponse<BeerLocation.LocationInfo> response = executeCall(getApi().loadLocationById(params1));
                subscriber.onNext(response.getModels().get(0));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
