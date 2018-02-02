package com.brewmapp.execution.task;

import android.util.Log;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.pojo.SubscriptionPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.paperdb.Paper;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadSubscriptionsListTask extends BaseNetworkTask<SubscriptionPackage, ListResponse<Subscription>> {

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
    protected Observable<ListResponse<Subscription>> prepareObservable(SubscriptionPackage subscriptionPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.SUBSCRIPTION);
                params.addParam(Keys.USER_ID, userRepo.load().getId());
                if(subscriptionPackage.getRelated_model()!=null)
                    params.addParam(Keys.RELATED_MODEL,subscriptionPackage.getRelated_model());
                if(subscriptionPackage.getRelated_id()!=null)
                    params.addParam(Keys.RELATED_ID,subscriptionPackage.getRelated_id());


                String key=new StringBuilder()
                        .append(getClass().toString())
                        .append(subscriptionPackage.getRelated_model())
                        .append(subscriptionPackage.getRelated_id())
                        .toString();
                ListResponse<Subscription>  o= null;
                if(subscriptionPackage.isCacheOn()) {
                    o = Paper.book().read(key);
                    if (o != null) {
                        subscriber.onNext(o);
                        Log.i("NetworkTask", "LoadSubscriptionsListTask - cache-read");
                    }
                }
                ListResponse<Subscription> response = executeCall(getApi().loadUserSubscriptionsList(params));
                Paper.book().write(key,response);
                Log.i("NetworkTask","LoadSubscriptionsListTask - cache-write");
                if(o==null)
                    subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
