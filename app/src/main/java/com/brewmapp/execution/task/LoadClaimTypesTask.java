package com.brewmapp.execution.task;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by oleg on 26.07.17.
 */

public class LoadClaimTypesTask extends BaseNetworkTask<Void, String[]> {


    @Inject
    public LoadClaimTypesTask(MainThread mainThread,
                              Executor executor,
                              Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String[]> prepareObservable(Void params) {
        return Observable.create(subscriber -> {
            try {
                SingleResponse<Map<String, String>> response = executeCall(getApi().claimTypes());
                String[] variants = new String[response.getData().size()];
                for(int i = 0; i < variants.length; i++) {
                    variants[i] = response.getData().get(String.valueOf(i));
                }
                subscriber.onNext(variants);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
