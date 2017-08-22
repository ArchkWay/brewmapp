package com.brewmapp.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Album;
import com.brewmapp.data.pojo.NewAlbumPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class CreateAlbumTask extends BaseNetworkTask<NewAlbumPackage, SingleResponse<Album>> {


    @Inject
    public CreateAlbumTask(MainThread mainThread,
                           Executor executor,
                           Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<SingleResponse<Album>> prepareObservable(NewAlbumPackage params) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams wrapperParams = new WrapperParams(Wrappers.PHOTO_ALBUM);
                wrapperParams.addParam(Keys.NAME, params.getName());
                wrapperParams.addParam(Keys.DESCRIPTION, params.getDescription());
                subscriber.onNext(executeCall(getApi().createAlbum(wrapperParams)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
