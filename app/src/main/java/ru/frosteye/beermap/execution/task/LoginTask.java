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
import rx.Observable;
import rx.Subscriber;

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
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
