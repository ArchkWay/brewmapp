package com.brewmapp.execution.task;

import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 11.03.2018.
 */

public class AddReviewsApprovalTask extends BaseNetworkTask<WrapperParams,String> {

    @Inject
    public AddReviewsApprovalTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String> prepareObservable(WrapperParams wrapperParams) {
        return Observable.create(subscriber -> {
            try {
                Object o=executeCall(getApi().addReviewsApproval(wrapperParams));
                subscriber.onNext("");
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }
}
