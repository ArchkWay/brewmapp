package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.container.AlbumPhotos;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadAlbumPhotosTask extends BaseNetworkTask<Integer, AlbumPhotos> {


    @Inject
    public LoadAlbumPhotosTask(MainThread mainThread,
                               Executor executor,
                               Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<AlbumPhotos> prepareObservable(Integer params) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams wrapperParams = new WrapperParams(Wrappers.PHOTO);
                wrapperParams.addParam(Keys.PHOTO_ALBUM_ID, params);
                subscriber.onNext(executeCall(getApi().loadPhotosForAlbum(wrapperParams)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
