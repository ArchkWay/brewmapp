package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.ovsa.execution.executor.MainThread;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by oleg on 26.07.17.
 */

public class LoginTask extends BaseNetworkTask<WrapperParams, MessageResponse> {

    @Inject
    public LoginTask(MainThread mainThread,
                     Executor executor,
                     Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<MessageResponse> prepareObservable(WrapperParams params) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(executeCall(getApi().login(params)));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
