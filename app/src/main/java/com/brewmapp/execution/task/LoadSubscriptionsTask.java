package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadSubscriptionsTask extends BaseNetworkTask<Integer, ListResponse<Subscription>> {

    private UserRepo userRepo;

    @Inject
    public LoadSubscriptionsTask(MainThread mainThread,
                                 Executor executor,
                                 Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<ListResponse<Subscription>> prepareObservable(Integer params) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params1 = new WrapperParams(Wrappers.SUBSCTIPRION);
                params1.addParam(Keys.USER_ID, userRepo.load().getId());
                ListResponse<Subscription> response = executeCall(getApi().loadUserSubscriptions(params1));
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
