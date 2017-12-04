package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Users;
import com.brewmapp.data.entity.wrapper.UserInfo;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadUsersTask extends BaseNetworkTask<Integer, ArrayList<User>> {

    @Inject
    public LoadUsersTask(MainThread mainThread,
                         Executor executor,
                         Api api) {
        super(mainThread, executor, api);

    }

    @Override
    protected Observable<ArrayList<User>> prepareObservable(Integer user_id) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.USER);
                params.addParam(Keys.ID, user_id);
                Users response = executeCall(getApi().getUsers(params));
                Iterator<UserInfo> userInfoIterator=response.getModels().iterator();
                ArrayList<User> userArrayList=new ArrayList<User>();
                while (userInfoIterator.hasNext())userArrayList.add(userInfoIterator.next().getModel());
                subscriber.onNext(userArrayList);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
