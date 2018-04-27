package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadSubscriptionsItemsTask extends BaseNetworkTask<Integer, Subscriptions> {

    private UserRepo userRepo;

    @Inject
    public LoadSubscriptionsItemsTask(MainThread mainThread,
                                      Executor executor,
                                      Api api,
                                      UserRepo userRepo
                                ) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<Subscriptions> prepareObservable(Integer user_id) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params1 = new WrapperParams(Wrappers.SUBSCRIPTION);
                if(user_id==0)
                    params1.addParam(Keys.USER_ID, userRepo.load().getId());
                else
                    params1.addParam(Keys.USER_ID,user_id);

                Subscriptions subscriptions = executeCall(getApi().loadUserSubscriptionsItems(params1));
                subscriber.onNext(subscriptions);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
