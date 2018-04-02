package com.brewmapp.execution.task;

import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

public class ActivetyUsers extends BaseNetworkTask<RequestParams, List<User>> {

    @Inject
    public ActivetyUsers(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<User>> prepareObservable(RequestParams requestParams) {
        return Observable.create(subscriber -> {
            try {
                ListResponse<User> listResponse=executeCall(getApi().getActivetyUsets(requestParams));
                subscriber.onNext(listResponse.getModels());
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }
}
