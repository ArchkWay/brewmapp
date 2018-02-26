package com.brewmapp.execution.task;

import java.io.File;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperMultipartParams;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import com.brewmapp.utils.events.markerCluster.MapUtils;

import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class CreatePostTask extends BaseNetworkTask<Post, SingleResponse<Post>> {

    private UserRepo userRepo;

    @Inject
    public CreatePostTask(MainThread mainThread,
                          Executor executor,
                          Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<SingleResponse<Post>> prepareObservable(Post post) {
        return Observable.create(subscriber -> {
            try {
                for(UploadPhotoResponse response: post.getFilesToUpload()) {
                    post.getPhotoIds().add(uploadFile(response.getFile(), response.getTitle()).getId());
                }
                WrapperParams wrapperParams = post.createParams();
                wrapperParams.addParam(Keys.RELATED_ID, userRepo.load().getId());
                if(post.getPhotoIds().size()>0)
                    wrapperParams.addParam(Keys.PHOTOS_ID, MapUtils.strJoin(post.getPhotoIds().toArray(),","));
                subscriber.onNext(executeCall(getApi().createPost(wrapperParams)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private UploadPhotoResponse uploadFile(File file, String title) {
        WrapperMultipartParams wrapperParams = new WrapperMultipartParams(Wrappers.PHOTO);
        wrapperParams.addPart(Keys.RELATED_MODEL, Keys.CAP_USER);
        wrapperParams.addPart(Keys.RELATED_ID, userRepo.load().getId());
        wrapperParams.addPart(Keys.IMAGE, file);
        if(title != null) {
            wrapperParams.addPart(Keys.TITLE, title);
        }
        SingleResponse<UploadPhotoResponse> response = executeCall(getApi().uploadPhoto(wrapperParams));
        return response.getData();
    }
}
