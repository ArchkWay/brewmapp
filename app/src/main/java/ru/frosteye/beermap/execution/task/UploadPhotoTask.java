package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.pojo.NewPhotoPackage;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperMultipartParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.UploadPhotoResponse;
import ru.frosteye.beermap.execution.exchange.response.base.SingleResponse;
import ru.frosteye.beermap.execution.task.base.BaseNetworkTask;
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
