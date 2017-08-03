package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
import rx.Observable;

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
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
