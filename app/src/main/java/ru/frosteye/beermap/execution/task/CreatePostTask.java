package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.Album;
import ru.frosteye.beermap.data.entity.Post;
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
                WrapperParams wrapperParams = post.createParams();
                wrapperParams.addParam(Keys.RELATED_ID, userRepo.load().getId());
                subscriber.onNext(executeCall(getApi().createPost(wrapperParams)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
