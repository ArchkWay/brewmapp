package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class DeleteAlbumTask extends BaseNetworkTask<Integer, MessageResponse> {

    private UserRepo userRepo;

    @Inject
    public DeleteAlbumTask(MainThread mainThread,
                           Executor executor,
                           Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<MessageResponse> prepareObservable(Integer params) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params1 = new WrapperParams(Wrappers.PHOTO_ALBUM);
                params1.addParam(Keys.ID, params);
                MessageResponse response = executeCall(getApi().deleteAlbum(params1));
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
