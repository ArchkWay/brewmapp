package com.brewmapp.execution.task;

import com.brewmapp.data.pojo.EvaluationPackage;
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
 * Created by xpusher on 11/3/2017.
 */

public class SetRestoEvaluationTask extends BaseNetworkTask<EvaluationPackage, String> {

    @Inject
    public SetRestoEvaluationTask(MainThread mainThread, Executor executor, Api api) {
        super(mainThread, executor, api);
    }

    @Override
    protected Observable<String> prepareObservable(EvaluationPackage evaluationPackage) {
        return Observable.create(subscriber -> {
            try {
                WrapperParams params=new WrapperParams(Wrappers.RESTO_EVALUATION);
                if(evaluationPackage.getEaluation_value()!=null) {
                    params.addParam(Keys.RESTO_ID, evaluationPackage.getModel_id());
                    params.addParam(Keys.EVLUATION_TYPE, evaluationPackage.getEaluation_type());
                    params.addParam(Keys.EVLUATION_VALUE, evaluationPackage.getEaluation_value());
                    Object o = executeCall(getApi().setRestoEvaluation(params));
                }
                subscriber.onNext("");
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
