package com.brewmapp.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.pojo.NewPhotoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperMultipartParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class UploadPhotoTask extends BaseNetworkTask<NewPhotoPackage, SingleResponse<UploadPhotoResponse>> {

    private UserRepo userRepo;

    @Inject
    public UploadPhotoTask(MainThread mainThread,
                           Executor executor,
                           Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<SingleResponse<UploadPhotoResponse>> prepareObservable(NewPhotoPackage params) {
        return Observable.create(subscriber -> {
            try {
                WrapperMultipartParams wrapperParams = new WrapperMultipartParams(Wrappers.PHOTO);
                wrapperParams.addPart(Keys.RELATED_MODEL, params.getRelatedModel());
                wrapperParams.addPart(Keys.RELATED_ID, params.getRelatedId() == 0 ? userRepo.load().getId() : params.getRelatedId());
                if(params.getAlbumId() != 0) {
                    wrapperParams.addPart(Keys.PHOTO_ALBUM_ID, params.getAlbumId());
                }
                wrapperParams.addPart(Keys.IMAGE, params.getFile());
                subscriber.onNext(executeCall(getApi().uploadPhoto(wrapperParams)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
