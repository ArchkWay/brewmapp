package com.brewmapp.execution.task;

import com.brewmapp.data.entity.container.Interests;
import com.brewmapp.data.entity.container.InterestsByUser;
import com.brewmapp.data.pojo.LoadInterestPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by xpusher on 1/16/2018.
 */

public class LoadUsersByInterestTask extends BaseNetworkTask<Integer,List<IFlexible>> {

    @Inject
    public LoadUsersByInterestTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<IFlexible>> prepareObservable(Integer interests_id) {
        return Observable.create(subscriber -> {
            try {
                RequestParams requestParams=new RequestParams();
                requestParams.addParam("UserInterestIDs",interests_id);



                InterestsByUser interests=executeCall(getApi().loadUsersByInterest(requestParams));
                subscriber.onNext(new ArrayList<>(interests.getModels()));
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    };
}
