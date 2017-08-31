package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 26.07.17.
 */

public class LikeTask extends BaseNetworkTask<LikeDislikePackage, MessageResponse> {

    private UserRepo userRepo;

    @Inject
    public LikeTask(MainThread mainThread,
                    Executor executor,
                    Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<MessageResponse> prepareObservable(LikeDislikePackage params) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params1 = params.createParams();
                params1.addParam(Keys.USER_ID, userRepo.load().getId());
                MessageResponse response = executeCall(getApi().likeDislike(params1));
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
