package com.brewmapp.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.UserResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;
import io.reactivex.Observable;
/**
 * Created by oleg on 26.07.17.
 */

public class LoginTask extends BaseNetworkTask<WrapperParams, UserResponse> {

    private UserRepo userRepo;

    @Inject
    public LoginTask(MainThread mainThread,
                     Executor executor,
                     Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<UserResponse> prepareObservable(WrapperParams params) {
        return Observable.create(subscriber -> {
            try {
                UserResponse response = executeCall(getApi().login(params));
                userRepo.save(response.getUser());
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
