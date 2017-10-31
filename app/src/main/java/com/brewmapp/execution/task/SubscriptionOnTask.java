package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 10/31/2017.
 */

public class SubscriptionOnTask extends BaseNetworkTask<SubscriptionPackage,String> {

    @Inject
    public SubscriptionOnTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String> prepareObservable(SubscriptionPackage subscriptionPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.SUBSCRIPTION);
                params.addParam(Keys.RELATED_MODEL,subscriptionPackage.getRelated_model());
                params.addParam(Keys.RELATED_ID,subscriptionPackage.getRelated_id());
                SingleResponse<Subscription> o=executeCall(getApi().addSubscription(params));
                subscriber.onNext(o.getData().getId());
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }
}
