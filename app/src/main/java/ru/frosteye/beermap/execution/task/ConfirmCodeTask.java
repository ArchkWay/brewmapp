package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.ovsa.execution.executor.MainThread;
import rx.Observable;

/**
 * Created by oleg on 26.07.17.
 */

public class ConfirmCodeTask extends BaseNetworkTask<WrapperParams, ResponseBody> {

    @Inject
    public ConfirmCodeTask(MainThread mainThread,
                           Executor executor,
                           Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<ResponseBody> prepareObservable(WrapperParams params) {
        return Observable.create(subscriber -> {
            try {
                ResponseBody response = executeCall(getApi().confirmCode(params));
                subscriber.onNext(response);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
