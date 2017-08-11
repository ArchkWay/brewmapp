package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
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
