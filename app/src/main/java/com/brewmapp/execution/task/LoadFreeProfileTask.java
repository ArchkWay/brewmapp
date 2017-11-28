package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
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

public class LoadFreeProfileTask extends BaseNetworkTask<Integer, UserProfile> {


    @Inject
    public LoadFreeProfileTask(MainThread mainThread,
                               Executor executor,
                               Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<UserProfile> prepareObservable(Integer user_id ){
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.USER);
                params.addParam(Keys.ID, user_id);
                ListResponse<User> response = executeCall(getApi().getProfile(params));
                subscriber.onNext(new UserProfile(response.getModels().get(0)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
