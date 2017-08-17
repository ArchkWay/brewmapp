package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.Album;
import ru.frosteye.beermap.data.pojo.NewAlbumPackage;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.base.SingleResponse;
import ru.frosteye.beermap.execution.task.base.BaseNetworkTask;
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
