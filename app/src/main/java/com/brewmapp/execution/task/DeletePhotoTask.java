package com.brewmapp.execution.task;

import android.app.Activity;

import com.brewmapp.data.entity.container.AlbumPhotos;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 2/26/2018.
 */

public class DeletePhotoTask extends BaseNetworkTask<WrapperParams, Integer> {

    @Inject
    public DeletePhotoTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<Integer> prepareObservable(WrapperParams wrapperParams) {
        return Observable.create(subscriber -> {
            try {
                Object o=executeCall(getApi().deletePhoto(wrapperParams));
                subscriber.onNext(Activity.RESULT_OK);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
