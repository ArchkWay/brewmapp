package ru.frosteye.beermap.execution.task;

import java.io.File;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.data.pojo.RegisterPackageWithPhoneAndPassword;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.UserResponse;
import ru.frosteye.beermap.execution.exchange.response.base.ListResponse;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;
import ru.frosteye.ovsa.execution.network.request.RequestParams;
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
