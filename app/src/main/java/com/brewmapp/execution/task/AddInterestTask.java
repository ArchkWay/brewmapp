package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.AddInterestPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 10/23/2017.
 */

public class AddInterestTask extends BaseNetworkTask<AddInterestPackage,String> {

    @Inject
    public AddInterestTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }


    @Override
    protected Observable<String> prepareObservable(AddInterestPackage addInterestPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.USER_INTEREST);
                params.addParam(Keys.RELATED_MODEL,addInterestPackage.getRelated_model());
                params.addParam(Keys.RELATED_ID,addInterestPackage.getRelated_id());
                params.addParam(Keys.TOKEN,addInterestPackage.getToken());
                Object o=executeCall(getApi().addInterest(params));
                subscriber.onNext("");
                subscriber.onComplete();
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }
}
