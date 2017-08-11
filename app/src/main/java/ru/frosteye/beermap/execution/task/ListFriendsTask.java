package ru.frosteye.beermap.execution.task;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.execution.exchange.common.Api;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.exchange.response.base.MessageResponse;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 26.07.17.
 */

public class ListFriendsTask extends BaseNetworkTask<Void, MessageResponse> {

    private UserRepo userRepo;

    @Inject
    public ListFriendsTask(MainThread mainThread,
                           Executor executor,
                           Api api, UserRepo userRepo) {
        super(mainThread, executor, api);
        this.userRepo = userRepo;
    }

    @Override
    protected Observable<MessageResponse> prepareObservable(Void v) {
        return Observable.create(subscriber -> {
            try {

                WrapperParams params = createParamsForType(1);
                MessageResponse response = executeCall(getApi().listFriends(params));
                params = createParamsForType(0);
                response = executeCall(getApi().listFriends(params));
                params = createParamsForType(2);
                response = executeCall(getApi().listFriends(params));
                subscriber.onNext(response);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private WrapperParams createParamsForType(int type) {
        WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
        wrapperParams.addParam(Keys.USER_ID, userRepo.load().getId());
        wrapperParams.addParam(Keys.STATUS, type);
        return wrapperParams;
    }
}
