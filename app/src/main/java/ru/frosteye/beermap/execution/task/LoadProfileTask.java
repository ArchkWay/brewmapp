package ru.frosteye.beermap.execution.task;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Call;
import ru.frosteye.beermap.data.db.contract.PostsRepo;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.data.entity.UserProfile;
import ru.frosteye.beermap.data.entity.container.Posts;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.exchange.response.base.ListResponse;
import ru.frosteye.beermap.execution.task.base.BaseNetworkTask;
import ru.frosteye.beermap.execution.task.base.LoaderTask;
import ru.frosteye.ovsa.data.storage.Repo;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadProfileTask extends BaseNetworkTask<Void, UserProfile> {

    private UserRepo userRepo;

    @Inject
    public LoadProfileTask(MainThread mainThread,
                           Executor executor,
                           Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<UserProfile> prepareObservable(Void aVoid) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.USER);
                params.addParam(Keys.ID, userRepo.load().getId());
                ListResponse<User> response = executeCall(getApi().getProfile(params));
                userRepo.save(response.getModels().get(0));
                subscriber.onNext(new UserProfile(response.getModels().get(0)));
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
