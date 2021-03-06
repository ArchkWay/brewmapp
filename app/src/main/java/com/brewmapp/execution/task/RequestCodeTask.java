package com.brewmapp.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
import io.reactivex.Observable;

/**
 * Created by oleg on 26.07.17.
 */

public class RequestCodeTask extends BaseNetworkTask<RequestParams, MessageResponse> {

    @Inject
    public RequestCodeTask(MainThread mainThread,
                           Executor executor,
                           Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<MessageResponse> prepareObservable(RequestParams params) {
        return Observable.create(subscriber -> {
            try {
                MessageResponse response = executeCall(getApi().requestCode(params));
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
