package com.brewmapp.execution.task;

import com.brewmapp.data.entity.Evaluation;
import com.brewmapp.data.pojo.RestoEvaluationPackage;
import com.brewmapp.execution.exchange.common.Api;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.base.ListResponse;
import com.brewmapp.execution.task.base.BaseNetworkTask;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import io.reactivex.Observable;
import ru.frosteye.ovsa.execution.executor.MainThread;

/**
 * Created by xpusher on 11/3/2017.
 */

public class LoadRestoEvaluationTask extends BaseNetworkTask<RestoEvaluationPackage, List<Evaluation>> {

    @Inject
    public LoadRestoEvaluationTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<List<Evaluation>> prepareObservable(RestoEvaluationPackage restoEvaluationPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params=new WrapperParams(Wrappers.RESTO_EVALUATION);
                params.addParam(Keys.RESTO_ID, restoEvaluationPackage.getResto_id());
                ListResponse<Evaluation> listResponse= executeCall(getApi().getRestoEvaluation(params));
                subscriber.onNext(listResponse.getModels());
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
