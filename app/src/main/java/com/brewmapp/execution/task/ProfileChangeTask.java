package com.brewmapp.execution.task;

import android.text.TextUtils;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by Kras on 10.11.2017.
 */

public class ProfileChangeTask extends BaseNetworkTask<User, ListResponse<User>> {

    private UserRepo userRepo;

    @Inject
    public ProfileChangeTask(MainThread mainThread, Executor executor, Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;

    }

    @Override
    protected Observable<ListResponse<User>> prepareObservable(User user) {
            return Observable.create(subscriber -> {
                try {

                    WrapperParams wrapperParams = new WrapperParams(Wrappers.USER);
                    if(!TextUtils.isEmpty(user.getFirstname()))
                        wrapperParams.addParam(Keys.FIRSTNAME,user.getFirstname());
                    if(!TextUtils.isEmpty(user.getLastname()))
                        wrapperParams.addParam(Keys.LASTNAME,user.getLastname());


                    ListResponse<User> response = executeCall(getApi().profileEdit(wrapperParams));
                    userRepo.save(response.getModels().get(0));

//                    userRepo.save(response.getModels().get(0));
//                    if(params.getAvatarPath() != null) {
//                        MultipartRequestParams avatar = new MultipartRequestParams();
//                        String key = "User[image]";
//                        avatar.addPart(key, new File(params.getAvatarPath()));
//                        MessageResponse messageResponse = executeCall(getApi().uploadAvatar(avatar));
//                        WrapperParams profile = new WrapperParams(Wrappers.USER);
//                        profile.addParam(Keys.ID, userRepo.load().getId());
//                        ListResponse<User> userResponse = executeCall(getApi().getProfile(profile));
//                        userRepo.save(userResponse.getModels().get(0));
//                    }
                    subscriber.onNext(response);
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            });
    }
}
