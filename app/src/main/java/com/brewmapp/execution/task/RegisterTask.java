package com.brewmapp.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.UserResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;
import io.reactivex.Observable;

/**
 * Created by oleg on 26.07.17.
 */

public class RegisterTask extends BaseNetworkTask<WrapperParams, UserResponse> {

    @Inject
    public RegisterTask(MainThread mainThread,
                        Executor executor,
                        Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<UserResponse> prepareObservable(WrapperParams params) {
        return Observable.create(subscriber -> {
            try {
                UserResponse response = executeCall(getApi().register(params));
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
