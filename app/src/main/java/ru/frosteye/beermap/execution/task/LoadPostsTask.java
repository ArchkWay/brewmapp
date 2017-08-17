package ru.frosteye.beermap.execution.task;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;
import ru.frosteye.beermap.data.db.contract.PostsRepo;
import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.data.entity.container.Albums;
import ru.frosteye.beermap.data.entity.container.Posts;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.task.base.LoaderTask;
import ru.frosteye.ovsa.data.storage.Repo;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadPostsTask extends LoaderTask<WrapperParams, Posts> {

    private PostsRepo postsRepo;

    @Inject
    public LoadPostsTask(MainThread mainThread,
                         Executor executor,
                         Api api, PostsRepo postsRepo) {
        super(mainThread, executor, api);
        this.postsRepo = postsRepo;
    }

    @NonNull
    @Override
    protected Repo<Posts> getRepo() {
        return postsRepo;
    }

    @Override
    protected boolean cachedFirst() {
        return true;
    }

    @NonNull
    @Override
    protected Call<Posts> getCall(WrapperParams params) {
        return getApi().loadPosts(params);
    }
}
