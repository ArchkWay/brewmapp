package com.brewmapp.execution.task;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.PostsRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.pojo.ProfileInfoPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadProfileAndPostsTask extends BaseNetworkTask<Void, ProfileInfoPackage> {

    private PostsRepo postsRepo;
    private UserRepo userRepo;
    private int step;

    @Inject
    public LoadProfileAndPostsTask(MainThread mainThread,
                                   Executor executor,
                                   Api api, PostsRepo postsRepo, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.postsRepo = postsRepo;
        this.userRepo = userRepo;
        this.step = ResourceHelper.getInteger(R.integer.config_pack_size_0);
    }

    @Override
    protected Observable<ProfileInfoPackage> prepareObservable(Void aVoid) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.USER);
                params.addParam(Keys.ID, userRepo.load().getId());
                ListResponse<User> response = executeCall(getApi().getProfile(params));
                User user = response.getModels().get(0);
                userRepo.save(user);

                params = new WrapperParams(Wrappers.SUBSCRIPTION);
                params.addParam(Keys.USER_ID, userRepo.load().getId());
                int subscriptionsCount = executeCall(getApi().loadUserSubscriptionsList(params)).getTotal();
                user.getCounts().setSubscriptions(subscriptionsCount);
                userRepo.save(user);

                params = new WrapperParams(Wrappers.NEWS);
                params.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
                params.addParam(Keys.RELATED_ID, userRepo.load().getId());
                params.addParam(Keys.USER_SUBSCRIPTION, userRepo.load().getId());
                int start = 0;
                int end = step;
                Posts posts = executeCall(getApi().loadPosts(start, end, params));
                subscriber.onNext(new ProfileInfoPackage(new UserProfile(response.getModels().get(0)), posts));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
