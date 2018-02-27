package com.brewmapp.execution.task;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import retrofit2.Call;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.PostsRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.LoaderTask;
import ru.frosteye.ovsa.data.storage.Repo;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadPostsTask extends LoaderTask<LoadPostsPackage, Posts> {

    private PostsRepo postsRepo;
    private int step;
    private UserRepo userRepo;

    @Inject
    public LoadPostsTask(MainThread mainThread,
                         Executor executor,
                         Api api, PostsRepo postsRepo, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.postsRepo = postsRepo;
        this.userRepo = userRepo;
        this.step = ResourceHelper.getInteger(R.integer.config_pack_size_0);
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

    @Override
    protected boolean disableCache() {
        return true;
    }

    @NonNull
    @Override
    protected Call<Posts> getCall(LoadPostsPackage request) {
        WrapperParams params = new WrapperParams(Wrappers.NEWS);
        params.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
        params.addParam(Keys.RELATED_ID, userRepo.load().getId());
        if(request.isSubs()) {
            params.addParam(Keys.USER_SUBSCRIPTION, userRepo.load().getId());
        }
        int start = request.getPage() * step;
        int end = request.getPage() * step + step;
        return getApi().loadPosts(start, end, params);
    }
}
