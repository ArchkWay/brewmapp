package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.NewPhotoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.response.UploadPhotoResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.io.File;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;

/**
 * Created by xpusher on 11/16/2017.
 */

public class UploadAvatarTask extends BaseNetworkTask<File, String> {

    @Inject
    public UploadAvatarTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String> prepareObservable(File file) {
        return Observable.create(subscriber -> {
            try {
                MultipartRequestParams avatar = new MultipartRequestParams();
                String key = "User[image]";
                avatar.addPart(key, file);
                MessageResponse messageResponse = executeCall(getApi().uploadAvatar(avatar));
                subscriber.onNext(messageResponse.getMessage());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

}
