package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
import io.reactivex.Observable;

/**
 * Created by oleg on 26.07.17.
 */

public class FbLoginTask extends BaseNetworkTask<RequestParams, UserResponse> {

    private UserRepo userRepo;

    @Inject
    public FbLoginTask(MainThread mainThread,
                       Executor executor,
                       Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<UserResponse> prepareObservable(RequestParams params) {
        return Observable.create(subscriber -> {
            try {
                UserResponse response = executeCall(getApi().fbLogin(params));
                userRepo.save(response.getUser());
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
