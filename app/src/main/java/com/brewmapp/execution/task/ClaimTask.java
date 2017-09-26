package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.ClaimPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
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

public class ClaimTask extends BaseNetworkTask<ClaimPackage, Boolean> {


    @Inject
    public ClaimTask(MainThread mainThread,
                     Executor executor,
                     Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<Boolean> prepareObservable(ClaimPackage pack) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params = new WrapperParams(Wrappers.CLAIM);
                params.addParam(Keys.RELATED_MODEL, pack.getModel());
                params.addParam(Keys.RELATED_ID, pack.getModelId());
                params.addParam(Keys.TYPE, pack.getVariantId());
                if(pack.getText() != null) {
                    params.addParam(Keys.TEXT, pack.getText());
                }
                SingleResponse<Map<String, String>> response = executeCall(getApi().claim(params));
                subscriber.onNext(true);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
