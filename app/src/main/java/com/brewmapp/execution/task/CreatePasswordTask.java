package com.brewmapp.execution.task;

import java.io.File;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.pojo.RegisterPackageWithPhoneAndPassword;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;
import io.reactivex.Observable;

/**
 * Created by oleg on 26.07.17.
 */

public class CreatePasswordTask extends BaseNetworkTask<RegisterPackageWithPhoneAndPassword, ListResponse<User>> {

    private UserRepo userRepo;

    @Inject
    public CreatePasswordTask(MainThread mainThread,
                              Executor executor,
                              Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<ListResponse<User>> prepareObservable(RegisterPackageWithPhoneAndPassword params) {
        return Observable.create(subscriber -> {
            try {

                WrapperParams wrapperParams = new WrapperParams(Wrappers.USER);
                wrapperParams.addParam(Keys.PASSWORD, params.getPassword());
                ListResponse<User> response = executeCall(getApi().createPassword(wrapperParams));
                userRepo.save(response.getModels().get(0));
                if(params.getAvatarPath() != null) {
                    MultipartRequestParams avatar = new MultipartRequestParams();
                    String key = "User[image]";
                    avatar.addPart(key, new File(params.getAvatarPath()));
                    MessageResponse messageResponse = executeCall(getApi().uploadAvatar(avatar));
                    WrapperParams profile = new WrapperParams(Wrappers.USER);
                    profile.addParam(Keys.ID, userRepo.load().getId());
                    ListResponse<User> userResponse = executeCall(getApi().getProfile(profile));
                    userRepo.save(userResponse.getModels().get(0));
                }
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
