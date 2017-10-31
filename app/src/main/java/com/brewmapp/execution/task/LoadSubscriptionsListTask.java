package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadSubscriptionsListTask extends BaseNetworkTask<Integer, ListResponse<Subscription>> {

    private UserRepo userRepo;

    @Inject
    public LoadSubscriptionsListTask(MainThread mainThread,
                                     Executor executor,
                                     Api api,
                                     UserRepo userRepo
                                ) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<ListResponse<Subscription>> prepareObservable(Integer params) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params1 = new WrapperParams(Wrappers.SUBSCRIPTION);
                params1.addParam(Keys.USER_ID, userRepo.load().getId());
                ListResponse<Subscription> response = executeCall(getApi().loadUserSubscriptionsList(params1));
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
