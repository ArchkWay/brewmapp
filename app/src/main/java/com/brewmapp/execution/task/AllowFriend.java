package com.brewmapp.execution.task;

import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

import static com.brewmapp.R.string.params;

/**
 * Created by xpusher on 11/29/2017.
 */

public class AllowFriend extends BaseNetworkTask<WrapperParams , String> {

    @Inject
    public AllowFriend(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String> prepareObservable(WrapperParams  params) {
        return Observable.create(subscriber -> {
            try {
                executeCall(getApi().allowFriend(params));
                subscriber.onNext("Ok");
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
